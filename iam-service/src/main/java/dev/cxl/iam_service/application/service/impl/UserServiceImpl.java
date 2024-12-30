package dev.cxl.iam_service.application.service.impl;

import java.text.ParseException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import dev.cxl.iam_service.application.dto.response.PageResponse;
import dev.cxl.iam_service.application.dto.response.UserResponse;
import dev.cxl.iam_service.application.mapper.ForgotPassWordCommand;
import dev.cxl.iam_service.application.mapper.UserMapper;
import dev.cxl.iam_service.application.mapper.UserRoleMapper;
import dev.cxl.iam_service.application.service.custom.UserService;
import dev.cxl.iam_service.domain.command.ConfirmCreateUserCommand;
import dev.cxl.iam_service.domain.command.UserCreationCommand;
import dev.cxl.iam_service.domain.command.UserReplacePassCommand;
import dev.cxl.iam_service.domain.command.UserUpdateCommand;
import dev.cxl.iam_service.domain.domainentity.User;
import dev.cxl.iam_service.domain.enums.UserAction;
import dev.cxl.iam_service.domain.repository.UserRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.UserEntity;
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

    private final UserRoleServiceImpl userRoleService;

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
            UserRoleServiceImpl userRoleService1,
            UserRoleMapper userRoleMapper,
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
        this.userRoleService = userRoleService1;
        this.roleService = roleService;
        this.tokenService = tokenService;
    }

    @Override
    public void createUser(UserCreationRequest request) {
        UserCreationCommand userCreationCommand = userMapper.toUserUserCreationCommand(request);
        if (userRepository.existsByUserMail(userCreationCommand.getUserMail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        List<String> listRolesExits = new ArrayList<>();
        if (request.getRolesId() != null && !request.getRolesId().isEmpty()) {
            listRolesExits = roleService.listRolesExit(request.getRolesId());
        }
        User user = new User(
                userCreationCommand, passwordEncoder, () -> userKCLService.createUserKCL(request), listRolesExits);
        // Save userRole
        twoFactorAuthService.sendCreatUser(user.getUserMail());
        userRepository.save(user);
    }

    @Override
    public void confirmCreateUser(String email, String otp) {
        User user = userRepository.getUserByEmail(email);
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
    public PageResponse<UserResponse> getAllUsers(int page, int size) {
        Sort sort = Sort.by("userID").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = userRepository.findAll(pageable);
        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(userMapper::toUserResponse)
                        .toList())
                .build();
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest request) {
        UserUpdateCommand userUpdateCommand = userMapper.toUserUpdateCommand(request);
        String userID = SecurityUtils.getAuthenticatedUserID();
        User user = userRepository.getUserByUserId(userID);
        user.update(userUpdateCommand);
        activityService.createHistoryActivity(user.getUserID(), UserAction.UPDATE_PROFILE);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getMyInfo() {
        String id = SecurityUtils.getAuthenticatedUserID();
        UserEntity user;
        if (idpEnable) {
            user = utilUser.finUserKCLId(id);
        } else {
            user = utilUser.finUserId(id);
        }
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getInfo(String id) {
        UserEntity user;
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
        String id = SecurityUtils.getAuthenticatedUserID();
        User user = userRepository.getUserByUserId(id);
        user.changePassword(command, passwordEncoder);

        // Save history activity
        activityService.createHistoryActivity(user.getUserID(), UserAction.CHANGE_PASSWORD);
        userRepository.save(user);
    }

    @Override
    public Boolean createProfile(MultipartFile file) {
        String id = SecurityUtils.getAuthenticatedUserID();
        User user = utilUser.getUserDomainById(id);
        APIResponse<String> apiResponse = client.createProfile(file, SecurityUtils.getAuthenticatedUserID());
        user.createProfile(apiResponse.getResult());
        userRepository.save(user);
        return true;
    }

    @Override
    public ResponseEntity<Resource> viewProfile() {
        String userID = SecurityUtils.getAuthenticatedUserID();
        User user = utilUser.getUserDomainById(userID);
        ResponseEntity<Resource> response = client.viewProfile(user.getProfile());
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @Override
    public void sendToken(String email) {
        utilUser.finUserMail(email);
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
        User user = utilUser.getUserDomainById(userid);
        user.setPassWord(passwordEncoder.encode(forgotPassWord.getNewPass()));
        userRepository.save(user);

        // Save history activity
        tokenService.invalidToken(signedJWT.getJWTClaimsSet().getJWTID());

        return true;
    }
}
