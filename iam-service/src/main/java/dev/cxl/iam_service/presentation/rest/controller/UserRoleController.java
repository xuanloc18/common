package dev.cxl.iam_service.presentation.rest.controller;

import org.springframework.web.bind.annotation.*;

import dev.cxl.iam_service.application.dto.response.APIResponse;
import dev.cxl.iam_service.domain.entity.UserRole;
import dev.cxl.iam_service.application.service.UserRoleService;

@RestController
@RequestMapping("user-role")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping
    public APIResponse<UserRole> createUserRole(
            @RequestParam("userMail") String userMail, @RequestParam("roleCode") String roleCode) {
        return APIResponse.<UserRole>builder()
                .result(userRoleService.createUserRole(userMail, roleCode))
                .build();
    }

    @PostMapping("{userroleid}/deleted")
    public APIResponse<Boolean> delete(@PathVariable("userroleid") String id) {
        return APIResponse.<Boolean>builder().result(userRoleService.delete(id)).build();
    }
}