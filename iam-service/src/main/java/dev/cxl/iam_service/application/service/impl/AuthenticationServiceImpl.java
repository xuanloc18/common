package dev.cxl.iam_service.application.service.impl;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;
import com.evo.common.webapp.security.TokenCacheService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import dev.cxl.iam_service.application.configuration.KeyProvider;
import dev.cxl.iam_service.application.dto.AuthenticationProperties;
import dev.cxl.iam_service.application.dto.identity.TokenExchangeResponseUser;
import dev.cxl.iam_service.application.dto.request.AuthenticationRequestTwo;
import dev.cxl.iam_service.application.dto.request.IntrospectRequest;
import dev.cxl.iam_service.application.dto.response.AuthenticationResponse;
import dev.cxl.iam_service.application.dto.response.DefaultClientTokenResponse;
import dev.cxl.iam_service.application.dto.response.IntrospectResponse;
import dev.cxl.iam_service.application.service.custom.AuthenticationService;
import dev.cxl.iam_service.domain.domainentity.User;
import dev.cxl.iam_service.domain.enums.UserAction;
import dev.cxl.iam_service.infrastructure.entity.UserEntity;
import dev.cxl.iam_service.infrastructure.persistent.JpaUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@EnableConfigurationProperties(AuthenticationProperties.class)
@RequiredArgsConstructor // là một annotation trong Lombok tự động tạo constructor cho các trường (fields) có giá trị là
// final hoặc được đánh dấu là @NonNull
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final JpaUserRepository userRespository;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected Long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected Long REFRESHABLE_DURATION;

    private final KeyProvider keyProvider;

    private final ActivityServiceImpl activityService;

    private final TwoFactorAuthServiceImpl twoFactorAuthService;

    private final UtilUserServiceImpl userUtil;
    private final TokenCacheService tokenCacheService;
    private final ClientsServiceImpl clientsService;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean valid = true;
        try {
            verifyToken(token);
        } catch (AppException appException) {
            valid = false;
        }
        return IntrospectResponse.builder().valid(valid).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequestTwo authenticationRequestTwo)
            throws ParseException {
        User user = userUtil.findUserMail(authenticationRequestTwo.getUserMail());
        Boolean check = twoFactorAuthService.validateOtp(authenticationRequestTwo);
        if (!check) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }
        // activity
        activityService.createHistoryActivity(user.getUserID(), UserAction.LOGIN);
        var token = generateToken(authenticationRequestTwo.getUserMail());
        SignedJWT signedJWT = SignedJWT.parse(token);
        String idToken = signedJWT.getJWTClaimsSet().getJWTID();
        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(generateRefreshToken(user.getUserID(), idToken))
                .authentication(true)
                .build();
    }

    public String generateToken(String mail) {
        User user = userUtil.findUserMail(mail);
        JWSHeader header = new JWSHeader(JWSAlgorithm.RS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserID().toString())
                .issuer("cxl")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("user_id", user.getUserID())
                .claim("given_name", user.getUserName())
                .claim("name", user.getUserName())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new RSASSASigner(keyProvider.getKeyPair().getPrivate()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("can not create token", e);
            throw new RuntimeException(e);
        }
    }

    // Token client
    public String generateClientToken(DefaultClientTokenResponse request) {
        clientsService.checkClientExists(request.getClientId(), request.getClientSecret());
        JWSHeader header = new JWSHeader(JWSAlgorithm.RS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .subject(request.getClientId())
                .claim("client_id", request.getClientId())
                .claim("user_id", request.getClientId())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new RSASSASigner(keyProvider.getKeyPair().getPrivate()));
            log.info("client created {}", jwsObject.serialize());
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("can not create token", e);
            throw new RuntimeException(e);
        }
    }

    public String generateRefreshToken(String userId, String idToken) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.RS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("idToken", idToken)
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new RSASSASigner(keyProvider.getKeyPair().getPrivate()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("can not create access token", e);
            throw new RuntimeException(e);
        }
    }

    public SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        RSASSAVerifier rsassaVerifier =
                new RSASSAVerifier((RSAPublicKey) keyProvider.getKeyPair().getPublic());
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean veri = signedJWT.verify(rsassaVerifier);
        if (!(veri && expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (tokenCacheService.isExistedToken(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    public void logout(String accessToken, String refreshToken) {
        try {
            accessToken = accessToken.replace("Bearer ", "");
            var signToken = verifyToken(accessToken);
            var signRefreshToken = verifyToken(refreshToken);
            String tokenID = signToken.getJWTClaimsSet().getJWTID();
            String refreshTokenID = signRefreshToken.getJWTClaimsSet().getJWTID();
            String userID = signToken.getJWTClaimsSet().getSubject();
            tokenCacheService.invalidRefreshToken(refreshTokenID);
            tokenCacheService.invalidToken(tokenID);

            // activity
            activityService.createHistoryActivity(userID, UserAction.LOGOUT);
        } catch (AppException exception) {
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public TokenExchangeResponseUser refreshToken(String refreshToken) throws ParseException, JOSEException {
        log.info("Received refresh token: " + refreshToken);
        SignedJWT signedJWT = SignedJWT.parse(refreshToken);
        if (tokenCacheService.isExistedRefreshToken(signedJWT.getJWTClaimsSet().getJWTID())) {
            log.info(
                    "Refresh token {}",
                    tokenCacheService.isExistedRefreshToken(
                            signedJWT.getJWTClaimsSet().getJWTID()));
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        verifyToken(refreshToken);
        String userId = signedJWT.getJWTClaimsSet().getSubject();
        String tokenId = signedJWT.getJWTClaimsSet().getStringClaim("idToken");
        log.info("Received refresh token: {}", tokenId);
        tokenCacheService.invalidToken(tokenId);
        tokenCacheService.invalidRefreshToken(signedJWT.getJWTClaimsSet().getJWTID());
        UserEntity user =
                userRespository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        var token = generateToken(user.getUserMail());
        SignedJWT signedJWT1 = SignedJWT.parse(token);
        return TokenExchangeResponseUser.builder()
                .accessToken(token)
                .refreshToken(generateRefreshToken(
                        userId, signedJWT1.getJWTClaimsSet().getJWTID()))
                .build();
    }
}
