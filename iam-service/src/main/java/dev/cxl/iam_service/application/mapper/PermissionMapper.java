package dev.cxl.iam_service.application.mapper;

import org.mapstruct.Mapper;

import dev.cxl.iam_service.application.dto.response.PermissionResponse;
import dev.cxl.iam_service.domain.domainentity.Permission;
import dev.cxl.iam_service.infrastructure.entity.PermissionEntity;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionEntity toPermission(Permission permissionDomain);

    Permission toPermissionDomain(PermissionEntity permission);

    PermissionResponse toPermissionResponse(PermissionEntity permission);
}
