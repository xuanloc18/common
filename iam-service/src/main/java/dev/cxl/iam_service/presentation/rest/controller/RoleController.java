package dev.cxl.iam_service.presentation.rest.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import dev.cxl.iam_service.application.dto.request.RoleRequest;
import dev.cxl.iam_service.application.dto.response.APIResponse;
import dev.cxl.iam_service.application.dto.response.PageResponse;
import dev.cxl.iam_service.application.dto.response.RoleResponse;
import dev.cxl.iam_service.application.service.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PreAuthorize("hasPermission('ROLE_DATA','CREATE')")
    @PostMapping
    APIResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return APIResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @PreAuthorize("hasPermission('ROLE_DATA','VIEW')")
    @GetMapping
    APIResponse<PageResponse<RoleResponse>> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pagesize", required = false, defaultValue = "10") int size) {
        return APIResponse.<PageResponse<RoleResponse>>builder()
                .result(roleService.getAll(page, size))
                .build();
    }

    @PreAuthorize("hasPermission('ROLE_DATA','DELETE')")
    @PostMapping("/{roleId}/deleted")
    APIResponse<Void> delete(@PathVariable String roleId) {
        roleService.delete(roleId);
        return APIResponse.<Void>builder().build();
    }
}
