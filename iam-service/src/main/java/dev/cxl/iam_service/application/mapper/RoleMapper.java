package dev.cxl.iam_service.application.mapper;

import org.mapstruct.Mapper;

import dev.cxl.iam_service.application.dto.response.RoleResponse;
import dev.cxl.iam_service.domain.domainentity.RoleDomain;
import dev.cxl.iam_service.infrastructure.entity.Role;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleDomain roleDomain);
    List<Role> toRoles(List<RoleDomain> roleDomains);
    RoleResponse toRoleResponse(Role role);

    RoleDomain toRoleDomain(Role role);
}
