package dev.cxl.iam_service.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import dev.cxl.iam_service.domain.domainentity.UserRoleDomain;
import dev.cxl.iam_service.infrastructure.entity.UserRole;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {
    List<UserRole> toUserRoles(List<UserRoleDomain> userRoleDomain);
    List<UserRoleDomain> toUserRoleDomain(List<UserRole> userRoles);


}
