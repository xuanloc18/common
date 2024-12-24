package dev.cxl.iam_service.presentation.rest.controller;

import org.springframework.web.bind.annotation.*;

import dev.cxl.iam_service.application.dto.response.APIResponse;
import dev.cxl.iam_service.domain.entity.RolePermission;
import dev.cxl.iam_service.application.service.RolePermissionService;

@RestController
@RequestMapping("/role-permission")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    public RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    @PostMapping
    public APIResponse<RolePermission> create(
            @RequestParam("roleCode") String roleCode,
            @RequestParam("perResource") String perResource,
            @RequestParam("perCode") String perCode) {
        return APIResponse.<RolePermission>builder()
                .result(rolePermissionService.create(roleCode, perResource, perCode))
                .build();
    }

    @PostMapping("/{roleperid}/deleted")
    public APIResponse<Boolean> deleted(@PathVariable("roleperid") String id) {
        return APIResponse.<Boolean>builder()
                .result(rolePermissionService.delete(id))
                .build();
    }
}
