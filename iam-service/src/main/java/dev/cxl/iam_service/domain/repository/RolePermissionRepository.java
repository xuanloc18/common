package dev.cxl.iam_service.domain.repository;

import java.util.List;
import java.util.Optional;

import dev.cxl.iam_service.infrastructure.entity.RolePermission;

public interface RolePermissionRepository {
    RolePermission save(RolePermission rolePermission);

    List<RolePermission> findByRoleId(String id);

    Optional<RolePermission> findById(String id);

    List<RolePermission> saveAll(List<RolePermission> rolePermissions);
}
