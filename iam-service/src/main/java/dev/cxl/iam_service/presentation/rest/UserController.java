package dev.cxl.iam_service.presentation.rest;

import java.text.ParseException;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import dev.cxl.iam_service.application.configuration.IdpConfig;
import dev.cxl.iam_service.application.dto.request.*;
import dev.cxl.iam_service.application.dto.response.APIResponse;
import dev.cxl.iam_service.application.dto.response.UserResponse;
import dev.cxl.iam_service.application.service.auth.DefaultServiceImpl;
import dev.cxl.iam_service.application.service.impl.UserServiceImpl;
import dev.cxl.iam_service.infrastructure.entity.UserEntity;
import dev.cxl.iam_service.infrastructure.respository.custom.UserRepositoryCustom;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    private final IdpConfig iidpConfig;

    private final DefaultServiceImpl defaultServiceImpl;

    private final UserRepositoryCustom userRepository;

    public UserController(
            UserServiceImpl userService,
            IdpConfig iidpConfig,
            DefaultServiceImpl defaultServiceImpl,
            UserRepositoryCustom userRepository) {
        this.userService = userService;
        this.iidpConfig = iidpConfig;
        this.defaultServiceImpl = defaultServiceImpl;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasPermission('USER_DATA','VIEW')")
    @GetMapping
    public APIResponse<Page<UserEntity>> getUsers(@ModelAttribute UserSearchRequest request) {
        return APIResponse.<Page<UserEntity>>builder()
                .result(userRepository.search(request))
                .build();
    }

    @PostMapping
    APIResponse<String> createUser(@RequestBody @Valid UserCreationRequest request) {
        iidpConfig.getAuthService().register(request);
        return APIResponse.<String>builder().result("Vào Mail xác nhận ").build();
    }

    @GetMapping("/confirm")
    APIResponse<String> confirmCreateUser(@RequestParam("email") String email, @RequestParam("otp") String otp) {
        userService.confirmCreateUser(email, otp);
        return APIResponse.<String>builder()
                .result("Đăng kí tài khoản thành công")
                .build();
    }

    @PreAuthorize("hasPermission('USER_DATA','UPDATE')")
    @PostMapping("/enable")
    APIResponse<String> enableUser(
            @RequestHeader("authorization") String token,
            @RequestParam("userID") String userID,
            @RequestBody @Valid UserUpdateRequest request)
            throws ParseException {
        iidpConfig.getAuthService().enableUser(token, userID, request);
        return APIResponse.<String>builder().result("enable thành công").build();
    }

    @PreAuthorize("hasPermission('USER_DATA','DELETE')")
    @PostMapping("/{userID}/deleted")
    APIResponse<String> deleteUser(@PathVariable("userID") String userID) {
        defaultServiceImpl.delete(userID);
        return APIResponse.<String>builder().result("thành công").build();
    }

    @GetMapping("/me")
    APIResponse<UserResponse> getUser() {
        return APIResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PreAuthorize("hasPermission('USER_DATA','VIEW')")
    @GetMapping("/{userID}")
    APIResponse<UserResponse> forgettingUser(@PathVariable String userID) {
        return APIResponse.<UserResponse>builder()
                .result(userService.getInfo(userID))
                .build();
    }

    @PreAuthorize("hasPermission('USER_DATA','UPDATE')")
    @PutMapping("reset-password")
    APIResponse<String> resetPassWord(
            @RequestHeader("authorization") String token,
            @RequestParam("userID") String userID,
            @RequestBody ResetPassword request)
            throws ParseException {
        iidpConfig.getAuthService().resetPassword(token, userID, request);
        return APIResponse.<String>builder().result("thành công").build();
    }

    @PutMapping("/password")
    APIResponse<String> changPass(@RequestBody UserReplacePass request) {
        userService.replacePassword(request);
        return APIResponse.<String>builder().result("Chang password successful").build();
    }

    @PutMapping({"/inform"})
    APIResponse<String> updateUser(@RequestBody UserUpdateRequest request) {
        userService.updateUser(request);
        return APIResponse.<String>builder().result("update successful").build();
    }

    @PostMapping("profile")
    public APIResponse<Boolean> createProfile(@RequestPart("file") MultipartFile file) {
        return APIResponse.<Boolean>builder()
                .result(userService.createProfile(file))
                .build();
    }

    @GetMapping("/profile")
    public ResponseEntity<?> viewProfile() {
        return userService.viewProfile();
    }

    @PostMapping("/role")
    public APIResponse<String> userAddRole(@RequestBody UserRoleRequest request) {
        userService.userAddRole(request);
        return APIResponse.<String>builder().result("Add UserRole successful").build();
    }

    @PostMapping("/role/deleted")
    public APIResponse<String> userDeleteRole(@RequestBody UserRoleRequest request) {
        userService.userDeleteRole(request);
        return APIResponse.<String>builder()
                .result("Delete UserRole successful")
                .build();
    }
}
