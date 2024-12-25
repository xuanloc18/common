package dev.cxl.iam_service.application.mapper;

import dev.cxl.iam_service.domain.domainentity.RoleDomain;
import org.mapstruct.Mapper;

import dev.cxl.iam_service.application.dto.request.RoleRequest;
import dev.cxl.iam_service.application.dto.response.RoleResponse;
import dev.cxl.iam_service.infrastructure.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleDomain roleDomain);
    RoleResponse toRoleResponse(Role role);
    RoleDomain toRoleDomain(Role role);

}
