package dev.cxl.iam_service.infrastructure.persistent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.cxl.iam_service.infrastructure.entity.RolePermissionEntity;

@Repository
public interface JpaRolePermissionRepository extends JpaRepository<RolePermissionEntity, String> {
    List<RolePermissionEntity> findByRoleId(String id);
}
