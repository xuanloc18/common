package dev.cxl.iam_service.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.cxl.iam_service.infrastructure.entity.Permission;

public interface PermissionRepository {
    Permission save(Permission permission);

    Page<Permission> findAll(Pageable pageable);

    Boolean existsByResourceCodeAndScope(String resourceCode, String scope);

    Optional<Permission> findByResourceCodeAndScope(String resourceCode, String scope);

    Optional<String> findPermissionIdByUserAndScope(String userID, String resourceCode, String scope);

    List<Permission> findPermissionIdByUser(String userID);

    Optional<Permission> findById(String id);
}
