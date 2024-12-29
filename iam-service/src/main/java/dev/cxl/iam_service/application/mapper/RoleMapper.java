package dev.cxl.iam_service.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import dev.cxl.iam_service.application.dto.response.RoleResponse;
import dev.cxl.iam_service.domain.domainentity.Role;
import dev.cxl.iam_service.infrastructure.entity.RoleEntity;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleEntity toRole(Role roleDomain);

    List<RoleEntity> toRoles(List<Role> roleDomains);

    RoleResponse toRoleResponse(RoleEntity role);

    Role toRoleDomain(RoleEntity role);
}
