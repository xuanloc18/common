package dev.cxl.iam_service.domain.repository;

import java.util.List;
import java.util.Optional;

import com.evo.common.DomainRepository;

import dev.cxl.iam_service.domain.domainentity.RolePermission;

public interface RolePermissionRepositoryDomain extends DomainRepository<RolePermission, String> {
    RolePermission save(RolePermission rolePermission);

    List<RolePermission> findByRoleId(String id);

    Optional<RolePermission> findById(String id);

    boolean saveAll(List<RolePermission> rolePermissions);
}
