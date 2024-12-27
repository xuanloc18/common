package dev.cxl.iam_service.application.mapper;

import org.mapstruct.Mapper;

import dev.cxl.iam_service.application.dto.response.PermissionResponse;
import dev.cxl.iam_service.domain.domainentity.PermissionDomain;
import dev.cxl.iam_service.infrastructure.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionDomain permissionDomain);

    PermissionDomain toPermissionDomain(Permission permission);

    PermissionResponse toPermissionResponse(Permission permission);
}
