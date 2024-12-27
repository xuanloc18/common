package dev.cxl.iam_service.application.mapper;

import dev.cxl.iam_service.domain.domainentity.RolePermissionDomain;
import dev.cxl.iam_service.domain.domainentity.UserRoleDomain;
import dev.cxl.iam_service.infrastructure.entity.RolePermission;
import dev.cxl.iam_service.infrastructure.entity.UserRole;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {
    List<RolePermission> toRolePermissions (List<RolePermissionDomain> rolePermissionDomains);
    List<RolePermissionDomain> toRolePermissionDomains(List<RolePermission> rolePermissions);
}
