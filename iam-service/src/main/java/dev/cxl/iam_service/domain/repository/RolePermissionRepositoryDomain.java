package dev.cxl.iam_service.domain.repository;

import java.util.List;
import java.util.Optional;

import dev.cxl.iam_service.infrastructure.entity.RolePermissionEntity;

public interface RolePermissionRepositoryDomain {
    RolePermissionEntity save(RolePermissionEntity rolePermission);

    List<RolePermissionEntity> findByRoleId(String id);

    Optional<RolePermissionEntity> findById(String id);

    List<RolePermissionEntity> saveAll(List<RolePermissionEntity> rolePermissions);
}
