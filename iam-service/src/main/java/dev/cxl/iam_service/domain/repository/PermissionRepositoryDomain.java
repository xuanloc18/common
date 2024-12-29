package dev.cxl.iam_service.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.cxl.iam_service.infrastructure.entity.PermissionEntity;

public interface PermissionRepositoryDomain {
    PermissionEntity save(PermissionEntity permission);

    Page<PermissionEntity> findAll(Pageable pageable);

    Boolean existsByResourceCodeAndScope(String resourceCode, String scope);

    Optional<PermissionEntity> findByResourceCodeAndScope(String resourceCode, String scope);

    Optional<String> findPermissionIdByUserAndScope(String userID, String resourceCode, String scope);

    List<PermissionEntity> findPermissionIdByUser(String userID);

    Optional<PermissionEntity> findById(String id);
}
