package dev.cxl.iam_service.presentation.rest;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import dev.cxl.iam_service.application.dto.response.APIResponse;
import dev.cxl.iam_service.application.service.impl.UserRoleServiceImpl;
import dev.cxl.iam_service.infrastructure.entity.UserRoleEntity;

@RestController
@RequestMapping("user-role")
public class UserRoleController {

    private final UserRoleServiceImpl userRoleService;

    public UserRoleController(UserRoleServiceImpl userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping
    public APIResponse<List<UserRoleEntity>> createUserRole(
            @RequestParam("userMail") String userMail, @RequestParam("roleCode") String roleCode) {
        return APIResponse.<List<UserRoleEntity>>builder()
                .result(userRoleService.createUserRole(userMail, roleCode))
                .build();
    }

    @PostMapping("{userprofile}/deleted")
    public APIResponse<Boolean> delete(@PathVariable("userroleid") String id) {
        return APIResponse.<Boolean>builder().result(userRoleService.delete(id)).build();
    }
}
