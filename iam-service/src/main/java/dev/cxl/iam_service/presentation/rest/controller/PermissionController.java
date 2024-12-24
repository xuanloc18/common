package dev.cxl.iam_service.presentation.rest.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import dev.cxl.iam_service.application.dto.request.PermissionRequest;
import dev.cxl.iam_service.application.dto.response.APIResponse;
import dev.cxl.iam_service.application.dto.response.PageResponse;
import dev.cxl.iam_service.application.dto.response.PermissionResponse;
import dev.cxl.iam_service.application.service.PermissionService;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PreAuthorize("hasPermission('PERMISSION_DATA','CREATE')")
    @PostMapping
    APIResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return APIResponse.<PermissionResponse>builder()
                .result(permissionService.createPermission(request))
                .build();
    }

    @PreAuthorize("hasPermission('PERMISSION_DATA','VIEW')")
    @GetMapping
    APIResponse<PageResponse<PermissionResponse>> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pagesize", required = false, defaultValue = "10") int size) {
        return APIResponse.<PageResponse<PermissionResponse>>builder()
                .result(permissionService.getListsPer(page, size))
                .build();
    }

    @PreAuthorize("hasPermission('PERMISSION_DATA','DELETE')")
    @PostMapping("/{permission}/deleted")
    APIResponse<Void> delete(@PathVariable String permission) {
        permissionService.delete(permission);
        return APIResponse.<Void>builder().build();
    }
}
