package dev.cxl.iam_service.application.mapper;

import org.mapstruct.Mapper;

import dev.cxl.iam_service.application.dto.request.RoleRequest;
import dev.cxl.iam_service.application.dto.response.RoleResponse;
import dev.cxl.iam_service.domain.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest roleRequest);

    RoleResponse toRoleResponse(Role role);
}
