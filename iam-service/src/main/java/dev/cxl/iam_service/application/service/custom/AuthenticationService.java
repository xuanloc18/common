package dev.cxl.iam_service.application.service.custom;

import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import dev.cxl.iam_service.application.dto.identity.TokenExchangeResponseUser;
import dev.cxl.iam_service.application.dto.request.AuthenticationRequestTwo;
import dev.cxl.iam_service.application.dto.request.IntrospectRequest;
import dev.cxl.iam_service.application.dto.response.AuthenticationResponse;
import dev.cxl.iam_service.application.dto.response.DefaultClientTokenResponse;
import dev.cxl.iam_service.application.dto.response.IntrospectResponse;

public interface AuthenticationService {

    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

    AuthenticationResponse authenticate(AuthenticationRequestTwo authenticationRequestTwo) throws ParseException;

    String generateToken(String mail);

    String generateClientToken(DefaultClientTokenResponse request);

    String generateRefreshToken(String userId, String idToken);

    SignedJWT verifyToken(String token) throws ParseException, JOSEException;

    void logout(String accessToken, String refreshToken);

    TokenExchangeResponseUser refreshToken(String refreshToken) throws ParseException, JOSEException;
}
