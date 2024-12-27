package dev.cxl.iam_service.application.service.auth;

import java.text.ParseException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.dto.identity.TokenExchangeResponseUser;
import dev.cxl.iam_service.application.dto.request.AuthenticationRequest;
import dev.cxl.iam_service.application.dto.request.ResetPassword;
import dev.cxl.iam_service.application.dto.request.UserCreationRequest;
import dev.cxl.iam_service.application.dto.request.UserUpdateRequest;
import dev.cxl.iam_service.application.service.UserKCLService;
import dev.cxl.iam_service.application.service.UserService;
import dev.cxl.iam_service.application.service.UtilUserService;
import dev.cxl.iam_service.infrastructure.entity.User;
import dev.cxl.iam_service.infrastructure.persistent.JpaUserRepository;

@Service
public class KCLServiceImpl implements IAuthService {

    private final UserService userService;

    private final UserKCLService userKCLService;

    private final JpaUserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UtilUserService utilUser;

    public KCLServiceImpl(
            UserService userService,
            UserKCLService userKCLService,
            JpaUserRepository userRespository,
            PasswordEncoder passwordEncoder,
            UtilUserService utilUser) {
        this.userService = userService;
        this.userKCLService = userKCLService;
        this.userRepository = userRespository;
        this.passwordEncoder = passwordEncoder;
        this.utilUser = utilUser;
    }

    @Override
    public Object login(AuthenticationRequest authenticationRequest) {
        return userKCLService.tokenExchangeResponseUser(authenticationRequest);
    }

    @Override
    public boolean logout(String token, String refreshToken) {
        userKCLService.logOut(token, refreshToken);
        return true;
    }

    @Override
    public boolean register(UserCreationRequest request) {
        userService.createUser(request);
        return true;
    }

    @Override
    public TokenExchangeResponseUser getRefreshToken(String refreshToken) {
        return userKCLService.refreshToken(refreshToken);
    }

    @Override
    public Boolean enableUser(String token, String id, UserUpdateRequest request) throws ParseException {
        User user = userRepository.findByUserKCLID(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setEnabled(request.getEnabled());
        userRepository.save(user);
        userKCLService.enableUser(userKCLService.tokenExchangeResponse().getAccessToken(), id, request);
        return null;
    }

    @Override
    public Boolean resetPassword(String token, String id, ResetPassword resetPassword) throws ParseException {
        User user = utilUser.finUserId(id);
        userKCLService.resetPassWord(
                userKCLService.tokenExchangeResponse().getAccessToken(), user.getUserKCLID(), resetPassword);
        user.setPassWord(passwordEncoder.encode(resetPassword.getValue()));
        userRepository.save(user);
        return null;
    }
}
