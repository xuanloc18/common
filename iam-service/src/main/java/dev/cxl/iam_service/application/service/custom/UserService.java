package dev.cxl.iam_service.application.service.custom;

import java.text.ParseException;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.nimbusds.jose.JOSEException;

import dev.cxl.iam_service.application.dto.request.*;
import dev.cxl.iam_service.application.dto.response.UserResponse;
import dev.cxl.iam_service.domain.domainentity.User;

public interface UserService {

    User createUser(UserCreationRequest request);

    void confirmCreateUser(String email, String otp);

    void updateUser(UserUpdateRequest request);

    UserResponse getMyInfo();

    UserResponse getInfo(String id);

    void replacePassword(UserReplacePass userRepalcePass);

    Boolean createProfile(MultipartFile file);

    ResponseEntity<Resource> viewProfile();

    void sendToken(String email);

    Boolean check(ForgotPassWord forgotPassWord) throws ParseException, JOSEException;
}
