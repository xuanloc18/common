package dev.cxl.iam_service.application.service.auth;

import java.text.ParseException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;

import dev.cxl.iam_service.application.dto.identity.TokenExchangeResponseUser;
import dev.cxl.iam_service.application.dto.request.AuthenticationRequest;
import dev.cxl.iam_service.application.dto.request.ResetPassword;
import dev.cxl.iam_service.application.dto.request.UserCreationRequest;
import dev.cxl.iam_service.application.dto.request.UserUpdateRequest;
import dev.cxl.iam_service.application.mapper.UserMapper;
import dev.cxl.iam_service.application.service.impl.*;
import dev.cxl.iam_service.domain.domainentity.User;
import dev.cxl.iam_service.domain.repository.UserRepositoryDomain;

@Service
public class DefaultServiceImpl implements IAuthService {

    private final AuthenticationServiceImpl authenticationService;

    private final TwoFactorAuthServiceImpl twoFactorAuthService;

    private final UserServiceImpl userService;

    private final UserRepositoryDomain userRepository;

    private final UserKCLServiceImpl userKCLService;

    private final UtilUserServiceImpl utilUser;

    private final PasswordEncoder passwordEncoder;

    public DefaultServiceImpl(
            AuthenticationServiceImpl authenticationService,
            TwoFactorAuthServiceImpl twoFactorAuthService,
            UserServiceImpl userService,
            UserRepositoryDomain userRepository,
            UserKCLServiceImpl userKCLService,
            UtilUserServiceImpl utilUser,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper) {
        this.authenticationService = authenticationService;
        this.twoFactorAuthService = twoFactorAuthService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.userKCLService = userKCLService;
        this.utilUser = utilUser;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Object login(AuthenticationRequest authenticationRequest) {
        twoFactorAuthService.sendOtpMail(authenticationRequest);
        return "OK";
    }

    @Override
    public boolean logout(String accessToken, String refreshToken) throws ParseException, JOSEException {
        authenticationService.logout(accessToken, refreshToken);
        return true;
    }

    @Override
    public boolean register(UserCreationRequest request) {
        userService.createUser(request);
        return true;
    }

    @Override
    public TokenExchangeResponseUser getRefreshToken(String refreshToken) throws ParseException, JOSEException {
        return authenticationService.refreshToken(refreshToken);
    }

    @Override
    public Boolean enableUser(String token, String id, UserUpdateRequest request) {
        User user = utilUser.getUserDomainById(id);
        user.enabled();
        userRepository.save(user);
        userKCLService.enableUser(
                userKCLService.tokenExchangeResponse().getAccessToken(), user.getUserKCLID(), request);
        return true;
    }

    public Boolean delete(String id) {
        User user = utilUser.getUserDomainById(id);
        user.deleted();
        userRepository.save(user);
        return true;
    }

    @Override
    public Boolean resetPassword(String token, String id, ResetPassword resetPassword) {
        User user = utilUser.getUserDomainById(id);
        user.forgotPassword(passwordEncoder.encode(resetPassword.getValue()));
        userRepository.save(user);
        userKCLService.resetPassWord(
                userKCLService.tokenExchangeResponse().getAccessToken(), user.getUserKCLID(), resetPassword);
        return true;
    }
}
