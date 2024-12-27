package dev.cxl.iam_service.infrastructure.persistent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.cxl.iam_service.infrastructure.entity.RolePermission;

@Repository
public interface JpaRolePermissionRepository extends JpaRepository<RolePermission, String> {
    List<RolePermission> findByRoleId(String id);
}
