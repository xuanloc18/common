package dev.cxl.iam_service.application.service;

import java.text.ParseException;
import java.util.*;

import dev.cxl.iam_service.application.dto.request.*;
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
import dev.cxl.iam_service.application.dto.response.PageResponse;
import dev.cxl.iam_service.application.dto.response.UserResponse;
import dev.cxl.iam_service.domain.entity.User;
import dev.cxl.iam_service.domain.enums.UserAction;
import dev.cxl.iam_service.application.mapper.UserMapper;
import dev.cxl.iam_service.infrastructure.respository.UserRespository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRespository userRespository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final AuthenticationService authenticationService;

    private final ActivityService activityService;

    private final TwoFactorAuthService twoFactorAuthService;

    private final UserKCLService userKCLService;

    private final UtilUserService utilUser;

    private final StorageClient client;

    @Value("${idp.enable}")
    Boolean idpEnable;

    private final TokenCacheService tokenService;

    public UserService(
            UserRespository userRespository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            AuthenticationService authenticationService,
            ActivityService activityService,
            TwoFactorAuthService twoFactorAuthService,
            UserKCLService userKCLService,
            UtilUserService utilUser,
            StorageClient client,
            TokenCacheService tokenService) {
        this.userRespository = userRespository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.authenticationService = authenticationService;
        this.activityService = activityService;
        this.twoFactorAuthService = twoFactorAuthService;
        this.userKCLService = userKCLService;
        this.utilUser = utilUser;
        this.client = client;
        this.tokenService = tokenService;
    }

    public UserResponse createUser(UserCreationRequest request) {
        if (userRespository.existsByUserMail(request.getUserMail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setUserKCLID(userKCLService.createUserKCL(request));
        user.setEnabled(false);
        user.setPassWord(passwordEncoder.encode(request.getPassWord()));
        twoFactorAuthService.sendCreatUser(user.getUserMail());
        return userMapper.toUserResponse(userRespository.save(user));
    }

    public UserResponse confirmCreateUser(String email, String otp) {
        User user = utilUser.finUserMail(email);
        Boolean check = twoFactorAuthService.validateOtp(
                AuthenticationRequestTwo.builder().userMail(email).otp(otp).build());
        if (!check) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }
        user.setEnabled(true);

        activityService.createHistoryActivity(user.getUserID(), UserAction.CREATE);

        return userMapper.toUserResponse(userRespository.save(user));
    }

    public PageResponse<UserResponse> getAllUsers(int page, int size) {
        Sort sort = Sort.by("userID").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = userRespository.findAll(pageable);
        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(user -> userMapper.toUserResponse(user))
                        .toList())
                .build();
    }

    public UserResponse updareUser(UserUpdateRequest request) {
        String userID = SecurityUtils.getAuthenticatedUserID();
        User user = utilUser.finUserId(userID);
        user = userMapper.updateUser(user, request);
        user.setPassWord(passwordEncoder.encode(request.getPassWord()));

        // Save history activity
        activityService.createHistoryActivity(user.getUserID(), UserAction.UPDATE_PROFILE);
        return userMapper.toUserResponse(userRespository.save(user));
    }

    public UserResponse getMyInfor() {
        String id = SecurityUtils.getAuthenticatedUserID();
        User user;
        if (idpEnable) {
            user = utilUser.finUserKCLId(id);
        } else {
            user = utilUser.finUserId(id);
        }

        return userMapper.toUserResponse(user);
    }

    public UserResponse getInfor(String id) {
        User user;
        if (idpEnable) {
            user = utilUser.finUserKCLId(id);
        } else {
            user = utilUser.finUserId(id);
        }

        return userMapper.toUserResponse(user);
    }

    public Boolean replacePassword(UserRepalcePass userRepalcePass) {
        String id = SecurityUtils.getAuthenticatedUserID();
        User user = utilUser.finUserId(id);
        log.info(user.getUserMail());
        log.info(user.getPassWord());
        Boolean checkPass = passwordEncoder.matches(userRepalcePass.getOldPassword(), user.getPassWord());
        if (!checkPass) throw new AppException(ErrorCode.INVALID_KEY);
        Boolean aBoolean = userRepalcePass.getConfirmPassword().equals(userRepalcePass.getNewPassword());
        if (!aBoolean) throw new RuntimeException("password does not confirm");

        user.setPassWord(passwordEncoder.encode(userRepalcePass.getNewPassword()));
        log.info(user.getPassWord());

        // Save history activity
        activityService.createHistoryActivity(user.getUserID(), UserAction.CHANGE_PASSWORD);
        userRespository.save(user);
        return true;
    }

    public Boolean createProfile(MultipartFile file) {
        String id = SecurityUtils.getAuthenticatedUserID();
        User user = utilUser.finUserId(id);
        APIResponse<String> apiResponse = client.createProfile(file, SecurityUtils.getAuthenticatedUserID());
        user.setProfile(apiResponse.getResult());
        userRespository.save(user);
        return true;
    }

    public ResponseEntity<Resource> viewProfile() {
        String userID = SecurityUtils.getAuthenticatedUserID();
        User user = utilUser.finUserId(userID);
        ResponseEntity<Resource> response = client.downloadFile(user.getProfile());
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    public String sendtoken(String email) {
        utilUser.finUserMail(email);
        String token = authenticationService.generateToken(email);
        emailService.SendEmail(email, token);
        return "Chúc bạn thành công";
    }

    public Boolean checkotp(ForgotPassWord forgotPassWord) throws ParseException, JOSEException {
        var respone = authenticationService.introspect(
                IntrospectRequest.builder().token(forgotPassWord.getToken()).build());
        if (!respone.isValid()) {
            return false;
        }
        SignedJWT signedJWT = SignedJWT.parse(forgotPassWord.getToken());
        String userid = signedJWT.getJWTClaimsSet().getSubject();
        User user = utilUser.finUserId(userid);
        user.setPassWord(passwordEncoder.encode(forgotPassWord.getNewPass()));
        userRespository.save(user);

        // Save history activity
        tokenService.invalidToken(signedJWT.getJWTClaimsSet().getJWTID());

        return true;
    }

    public List<UserResponse> findUserByKey(String keyKey, int page, int size, Object attribute, String key) {
        Pageable pageable = PageRequest.of(page - 1, size, sort(attribute, key));
        Page<User> user = userRespository.findUsersByKey(keyKey, pageable);
        return user.getContent().stream()
                .map(user1 -> userMapper.toUserResponse(user1))
                .toList();
    }

    public Sort sort(Object attribute, String key) {
        if (key.equals("DS")) {
            return Sort.by((String) attribute).descending();
        }
        return Sort.by(attribute.toString()).ascending();
    }
}
