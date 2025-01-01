package dev.cxl.iam_service.application.service.impl;

import java.text.ParseException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.evo.common.client.storage.StorageClient;
import com.evo.common.dto.response.APIResponse;
import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;
import com.evo.common.webapp.security.TokenCacheService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import dev.cxl.iam_service.application.configuration.SecurityUtils;
import dev.cxl.iam_service.application.dto.request.*;
import dev.cxl.iam_service.application.dto.response.UserResponse;
import dev.cxl.iam_service.application.mapper.UserMapper;
import dev.cxl.iam_service.application.service.custom.UserService;
import dev.cxl.iam_service.domain.command.*;
import dev.cxl.iam_service.domain.command.ForgotPassWordCommand;
import dev.cxl.iam_service.domain.domainentity.User;
import dev.cxl.iam_service.domain.enums.UserAction;
import dev.cxl.iam_service.domain.repository.UserRepositoryDomain;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepositoryDomain userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final EmailServiceImpl emailService;

    private final AuthenticationServiceImpl authenticationService;

    private final ActivityServiceImpl activityService;

    private final TwoFactorAuthServiceImpl twoFactorAuthService;

    private final UserKCLServiceImpl userKCLService;

    private final UtilUserServiceImpl utilUser;

    private final StorageClient client;

    private final RoleServiceImpl roleService;

    @Value("${idp.enable}")
    Boolean idpEnable;

    private final TokenCacheService tokenService;

    public UserServiceImpl(
            UserRepositoryDomain userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            EmailServiceImpl emailService,
            AuthenticationServiceImpl authenticationService,
            ActivityServiceImpl activityService,
            TwoFactorAuthServiceImpl twoFactorAuthService,
            UserKCLServiceImpl userKCLService,
            UtilUserServiceImpl utilUser,
            StorageClient client,
            RoleServiceImpl roleService,
            TokenCacheService tokenService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.authenticationService = authenticationService;
        this.activityService = activityService;
        this.twoFactorAuthService = twoFactorAuthService;
        this.userKCLService = userKCLService;
        this.utilUser = utilUser;
        this.client = client;
        this.roleService = roleService;
        this.tokenService = tokenService;
    }

    @Override
    public User createUser(UserCreationRequest request) {
        UserCreationCommand userCreationCommand = userMapper.toUserUserCreationCommand(request);
        userCreationCommand.setPassWord(passwordEncoder.encode(request.getPassWord()));
        if (userRepository.existsByUserMail(userCreationCommand.getUserMail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        List<String> listRolesExits = new ArrayList<>();
        if (request.getRoleCode() != null && !request.getRoleCode().isEmpty()) {
            listRolesExits = roleService.listRolesExit(request.getRoleCode());
        }
        User user = new User(userCreationCommand, () -> userKCLService.createUserKCL(request), listRolesExits);
        // Save userRole
        twoFactorAuthService.sendCreatUser(user.getUserMail());
        return userRepository.save(user);
    }

    @Override
    public void confirmCreateUser(String email, String otp) {
        User user = utilUser.findUserMail(email);
        ConfirmCreateUserCommand confirmCreateUserCommand = new ConfirmCreateUserCommand(email, otp);
        Boolean check = twoFactorAuthService.validateOtp(AuthenticationRequestTwo.builder()
                .userMail(confirmCreateUserCommand.getUserMail())
                .otp(confirmCreateUserCommand.getOtpCode())
                .build());
        if (!check) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }
        user.enabled();
        activityService.createHistoryActivity(user.getUserID(), UserAction.CREATE);
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserUpdateRequest request) {
        UserUpdateCommand userUpdateCommand = userMapper.toUserUpdateCommand(request);
        String userID = SecurityUtils.getAuthenticatedUserID();
        User user = utilUser.finUserId(userID);
        user.update(userUpdateCommand);
        activityService.createHistoryActivity(user.getUserID(), UserAction.UPDATE_PROFILE);
        userRepository.save(user);
    }

    @Override
    public UserResponse getMyInfo() {
        String id = SecurityUtils.getAuthenticatedUserID();
        User user;
        if (idpEnable) {
            user = utilUser.finUserKCLId(id);
        } else {
            user = utilUser.finUserId(id);
        }
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getInfo(String id) {
        User user;
        if (idpEnable) {
            user = utilUser.finUserKCLId(id);
        } else {
            user = utilUser.finUserId(id);
        }

        return userMapper.toUserResponse(user);
    }

    @Override
    public void replacePassword(UserReplacePass userReplacePass) {
        UserReplacePassCommand command = userMapper.toUserReplacePassCommand(userReplacePass);
        command.setConfirmPassword(passwordEncoder.encode(command.getConfirmPassword()));
        command.setOldPassword(passwordEncoder.encode(command.getOldPassword()));
        command.setNewPassword(passwordEncoder.encode(command.getNewPassword()));
        String id = SecurityUtils.getAuthenticatedUserID();
        User user = utilUser.finUserId(id);
        user.changePassword(command);

        // Save history activity
        activityService.createHistoryActivity(user.getUserID(), UserAction.CHANGE_PASSWORD);
        userRepository.save(user);
    }

    @Override
    public Boolean createProfile(MultipartFile file) {
        String id = SecurityUtils.getAuthenticatedUserID();
        User user = utilUser.finUserId(id);
        APIResponse<String> apiResponse = client.createProfile(file, SecurityUtils.getAuthenticatedUserID());
        user.createProfile(apiResponse.getResult());
        userRepository.save(user);
        return true;
    }

    @Override
    public ResponseEntity<Resource> viewProfile() {
        String userID = SecurityUtils.getAuthenticatedUserID();
        User user = utilUser.finUserId(userID);
        ResponseEntity<Resource> response = client.viewProfile(user.getProfile());
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @Override
    public void sendToken(String email) {
        utilUser.findUserMail(email);
        String token = authenticationService.generateToken(email);
        emailService.SendEmail(email, token);
    }

    @Override
    public Boolean check(ForgotPassWord forgotPassWord) throws ParseException, JOSEException {
        ForgotPassWordCommand forgotPassWordCommand =
                new ForgotPassWordCommand(forgotPassWord.getToken(), forgotPassWord.getNewPass());
        var response = authenticationService.introspect(IntrospectRequest.builder()
                .token(forgotPassWordCommand.getToken())
                .build());
        if (!response.isValid()) {
            return false;
        }
        SignedJWT signedJWT = SignedJWT.parse(forgotPassWord.getToken());
        String userid = signedJWT.getJWTClaimsSet().getSubject();
        User user = utilUser.finUserId(userid);
        user.forgotPassword(passwordEncoder.encode(forgotPassWord.getNewPass()));
        userRepository.save(user);

        // Save history activity
        tokenService.invalidToken(signedJWT.getJWTClaimsSet().getJWTID());

        return true;
    }

    public void userAddRole(UserRoleRequest userRoleRequest) {
        User user = utilUser.findUserMail(userRoleRequest.getUserMail());
        UserRoleCommand userRoleCommand = userMapper.toUserRoleCommand(userRoleRequest);
        List<String> roleIDs = roleService.listRolesExit(userRoleCommand.getRoleCodes());
        user.addUserRole(roleIDs);
        userRepository.save(user);
    }

    public void userDeleteRole(UserRoleRequest userRoleRequest) {
        User user = utilUser.findUserMail(userRoleRequest.getUserMail());
        UserRoleCommand userRoleCommand = userMapper.toUserRoleCommand(userRoleRequest);
        List<String> roleIDs = roleService.listRolesExit(userRoleCommand.getRoleCodes());
        user.deleteUserRole(roleIDs);
        userRepository.save(user);
    }
}
