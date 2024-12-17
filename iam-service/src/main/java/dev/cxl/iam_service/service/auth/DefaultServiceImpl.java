package dev.cxl.iam_service.service.auth;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;

import dev.cxl.iam_service.dto.identity.TokenExchangeResponseUser;
import dev.cxl.iam_service.dto.request.AuthenticationRequest;
import dev.cxl.iam_service.dto.request.ResetPassword;
import dev.cxl.iam_service.dto.request.UserCreationRequest;
import dev.cxl.iam_service.dto.request.UserUpdateRequest;
import dev.cxl.iam_service.entity.User;
import dev.cxl.iam_service.respository.UserRespository;
import dev.cxl.iam_service.service.*;

@Service
public class DefaultServiceImpl implements IAuthService {
    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    TwoFactorAuthService twoFactorAuthService;

    @Autowired
    UserService userService;

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private UserKCLService userKCLService;

    @Autowired
    private UtilUserService utilUser;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public Boolean enableUser(String token, String id, UserUpdateRequest request) throws ParseException {
        User user = utilUser.finUserId(id);
        user.setEnabled(request.getEnabled());
        userRespository.save(user);
        userKCLService.enableUser(
                userKCLService.tokenExchangeResponse().getAccessToken(), user.getUserKCLID(), request);
        return true;
    }

    public Boolean delete(String id) {
        User user = utilUser.finUserId(id);
        user.setDeleted(true);
        userRespository.save(user);
        return true;
    }

    @Override
    public Boolean resetPassword(String token, String id, ResetPassword resetPassword) throws ParseException {
        User user = utilUser.finUserId(id);
        user.setPassWord(passwordEncoder.encode(resetPassword.getValue()));
        userRespository.save(user);
        userKCLService.resetPassWord(
                userKCLService.tokenExchangeResponse().getAccessToken(), user.getUserKCLID(), resetPassword);
        return true;
    }
}
