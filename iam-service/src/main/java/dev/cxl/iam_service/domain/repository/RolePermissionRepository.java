package dev.cxl.iam_service.domain.repository;

import dev.cxl.iam_service.infrastructure.entity.RolePermission;

import java.util.List;
import java.util.Optional;

public interface RolePermissionRepository  {
    RolePermission save(RolePermission rolePermission);
    List<RolePermission> findByRoleId(String id);
    Optional<RolePermission> findById(String id);
}
