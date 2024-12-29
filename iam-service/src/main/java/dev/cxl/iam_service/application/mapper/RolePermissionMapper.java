package dev.cxl.iam_service.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import dev.cxl.iam_service.domain.domainentity.RolePermission;
import dev.cxl.iam_service.infrastructure.entity.RolePermissionEntity;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {
    List<RolePermissionEntity> toRolePermissions(List<RolePermission> rolePermissionDomains);

    List<RolePermission> toRolePermissionDomains(List<RolePermissionEntity> rolePermissions);
}
