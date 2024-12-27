package dev.cxl.iam_service.application.service;

import java.text.ParseException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
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
import dev.cxl.iam_service.application.mapper.UserMapper;
import dev.cxl.iam_service.application.mapper.UserRoleMapper;
import dev.cxl.iam_service.domain.domainentity.UserDomain;
import dev.cxl.iam_service.domain.enums.UserAction;
import dev.cxl.iam_service.domain.repository.UserRepository;
import dev.cxl.iam_service.infrastructure.entity.User;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final AuthenticationService authenticationService;

    private final ActivityService activityService;

    private final TwoFactorAuthService twoFactorAuthService;

    private final UserKCLService userKCLService;

    private final UtilUserService utilUser;

    private final StorageClient client;

    private final UserRoleService userRoleService;

    private final UserRoleMapper userRoleMapper;

    private final RoleService roleService;

    @Value("${idp.enable}")
    Boolean idpEnable;

    private final TokenCacheService tokenService;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            AuthenticationService authenticationService,
            ActivityService activityService,
            TwoFactorAuthService twoFactorAuthService,
            UserKCLService userKCLService,
            UtilUserService utilUser,
            StorageClient client,
            UserRoleService userRoleService1,
            UserRoleMapper userRoleMapper,
            RoleService roleService,
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
        this.userRoleMapper = userRoleMapper;
        this.roleService = roleService;
        this.tokenService = tokenService;
    }

    public void createUser(UserCreationRequest request) {
        if (userRepository.existsByUserMail(request.getUserMail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        List<String> listRolesExits = roleService.listRolesExit(request.getRolesId());
        UserDomain userDomain =
                new UserDomain(request, passwordEncoder, () -> userKCLService.createUserKCL(request), listRolesExits);

        User user = userMapper.toUser(userDomain);
        // Save userRole
        if (request.getRolesId() != null && !request.getRolesId().isEmpty()) {
            userRoleService.saveAllUserRoles(userDomain.getUserRoles());
        }
        twoFactorAuthService.sendCreatUser(user.getUserMail());
        userRepository.save(user);
    }

    public void confirmCreateUser(String email, String otp) {
        User user = utilUser.finUserMail(email);
        Boolean check = twoFactorAuthService.validateOtp(
                AuthenticationRequestTwo.builder().userMail(email).otp(otp).build());
        if (!check) {
            throw new AppException(ErrorCode.INVALID_OTP);


        }
        UserDomain userDomain = userMapper.toUserDomain(user);
        userDomain.enabled();
        activityService.createHistoryActivity(userDomain.getUserID(), UserAction.CREATE);
        userRepository.save(userMapper.toUser(userDomain));
    }

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

    public UserResponse updateUser(UserUpdateRequest request) {
        String userID = SecurityUtils.getAuthenticatedUserID();
        User user = utilUser.finUserId(userID);
        UserDomain userDomain = userMapper.toUserDomain(user);
        userDomain.update(request);
        user = userMapper.toUser(userDomain);
        // Save history activity
        activityService.createHistoryActivity(user.getUserID(), UserAction.UPDATE_PROFILE);
        return userMapper.toUserResponse(userRepository.save(user));
    }

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

    public UserResponse getInfo(String id) {
        User user;
        if (idpEnable) {
            user = utilUser.finUserKCLId(id);
        } else {
            user = utilUser.finUserId(id);
        }

        return userMapper.toUserResponse(user);
    }

    public void replacePassword(UserRepalcePass userRepalcePass) {
        String id = SecurityUtils.getAuthenticatedUserID();
        User user = utilUser.finUserId(id);
        UserDomain userDomain = userMapper.toUserDomain(user);
        userDomain.changePassword(userRepalcePass, passwordEncoder);
        User user2 = userMapper.toUser(userDomain);
        // Save history activity
        activityService.createHistoryActivity(user.getUserID(), UserAction.CHANGE_PASSWORD);
        userRepository.save(user2);
    }

    public Boolean createProfile(MultipartFile file) {
        String id = SecurityUtils.getAuthenticatedUserID();
        User user = utilUser.finUserId(id);
        UserDomain userDomain = userMapper.toUserDomain(user);
        APIResponse<String> apiResponse = client.createProfile(file, SecurityUtils.getAuthenticatedUserID());
        userDomain.createProfile(apiResponse.getResult());
        userRepository.save(userMapper.toUser(userDomain));
        return true;
    }

    public ResponseEntity<Resource> viewProfile() {
        String userID = SecurityUtils.getAuthenticatedUserID();
        User user = utilUser.finUserId(userID);
        UserDomain userDomain = userMapper.toUserDomain(user);
        ResponseEntity<Resource> response = client.downloadFile(userDomain.getProfile());
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    public void sendToken(String email) {
        utilUser.finUserMail(email);
        String token = authenticationService.generateToken(email);
        emailService.SendEmail(email, token);
    }

    public Boolean check(ForgotPassWord forgotPassWord) throws ParseException, JOSEException {
        var response = authenticationService.introspect(
                IntrospectRequest.builder().token(forgotPassWord.getToken()).build());
        if (!response.isValid()) {
            return false;
        }
        SignedJWT signedJWT = SignedJWT.parse(forgotPassWord.getToken());
        String userid = signedJWT.getJWTClaimsSet().getSubject();
        User user = utilUser.finUserId(userid);
        user.setPassWord(passwordEncoder.encode(forgotPassWord.getNewPass()));
        userRepository.save(user);

        // Save history activity
        tokenService.invalidToken(signedJWT.getJWTClaimsSet().getJWTID());

        return true;
    }

    public List<UserResponse> findUserByKey(String keyKey, int page, int size, Object attribute, String key) {
        Pageable pageable = PageRequest.of(page - 1, size, sort(attribute, key));
        Page<User> user = userRepository.findUsersByKey(keyKey, pageable);
        return user.getContent().stream().map(userMapper::toUserResponse).toList();
    }

    public Sort sort(Object attribute, String key) {
        if (key.equals("DS")) {
            return Sort.by((String) attribute).descending();
        }
        return Sort.by(attribute.toString()).ascending();
    }
}
