package dev.cxl.iam_service.application.mapper;

import org.mapstruct.Mapper;

import dev.cxl.iam_service.application.dto.request.PermissionRequest;
import dev.cxl.iam_service.application.dto.response.PermissionResponse;
import dev.cxl.iam_service.domain.command.PermissionCommand;
import dev.cxl.iam_service.domain.domainentity.Permission;
import dev.cxl.iam_service.infrastructure.entity.PermissionEntity;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionEntity toPermissionEntity(Permission permissionDomain);

    Permission toPermissionDomain(PermissionEntity permission);

    Permission toPermission(PermissionEntity permission);

    PermissionCommand toPermissionCommand(PermissionRequest permissionRequest);

    PermissionResponse toPermissionResponse(PermissionEntity permission);
}
