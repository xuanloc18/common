package dev.cxl.iam_service.presentation.rest;

import org.springframework.web.bind.annotation.*;

import dev.cxl.iam_service.application.dto.response.APIResponse;
import dev.cxl.iam_service.application.service.UserRoleService;
import dev.cxl.iam_service.infrastructure.entity.UserRole;

import java.util.List;

@RestController
@RequestMapping("user-role")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping
    public APIResponse<List<UserRole>> createUserRole(
            @RequestParam("userMail") String userMail, @RequestParam("roleCode") String roleCode) {
        return APIResponse.<List<UserRole>>builder()
                .result(userRoleService.createUserRole(userMail, roleCode))
                .build();
    }

    @PostMapping("{userroleid}/deleted")
    public APIResponse<Boolean> delete(@PathVariable("userroleid") String id) {
        return APIResponse.<Boolean>builder().result(userRoleService.delete(id)).build();
    }
}
