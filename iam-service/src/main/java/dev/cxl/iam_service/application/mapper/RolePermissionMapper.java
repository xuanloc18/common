package dev.cxl.iam_service.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import dev.cxl.iam_service.application.dto.request.RolePermissionRequest;
import dev.cxl.iam_service.domain.command.RolePermissionCommand;
import dev.cxl.iam_service.domain.domainentity.RolePermission;
import dev.cxl.iam_service.infrastructure.entity.RolePermissionEntity;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {
    RolePermission toRolePermission(RolePermissionEntity rolePermissionEntity);

    RolePermissionEntity toRolePermissionEntity(RolePermission rolePermission);

    List<RolePermission> toRolePermissions(List<RolePermissionEntity> rolePermissionEntities);

    List<RolePermissionEntity> toRolePermissionEntities(List<RolePermission> rolePermissions);

    RolePermissionCommand rolePermissionToRolePermissionCommand(RolePermissionRequest rolePermission);
}
