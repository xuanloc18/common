package dev.cxl.iam_service.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import dev.cxl.iam_service.domain.domainentity.UserRole;
import dev.cxl.iam_service.infrastructure.entity.UserRoleEntity;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {
    List<UserRoleEntity> toUserRoles(List<UserRole> userRoleDomain);

    List<UserRole> toUserRoleDomain(List<UserRoleEntity> userRoles);
}
