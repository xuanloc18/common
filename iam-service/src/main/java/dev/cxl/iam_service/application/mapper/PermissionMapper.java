package dev.cxl.iam_service.application.mapper;

import org.mapstruct.Mapper;

import dev.cxl.iam_service.application.dto.request.PermissionRequest;
import dev.cxl.iam_service.application.dto.response.PermissionResponse;
import dev.cxl.iam_service.domain.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);

    PermissionResponse toPermissionResponse(Permission permission);
}
