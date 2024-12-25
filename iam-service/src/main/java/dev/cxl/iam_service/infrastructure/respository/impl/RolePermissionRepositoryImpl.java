package dev.cxl.iam_service.infrastructure.respository.impl;

import dev.cxl.iam_service.domain.repository.RolePermissionRepository;
import dev.cxl.iam_service.infrastructure.entity.RolePermission;
import dev.cxl.iam_service.infrastructure.persistent.JpaRolePermissionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RolePermissionRepositoryImpl implements RolePermissionRepository {
    private final JpaRolePermissionRepository rolePermissionRepository;

    public RolePermissionRepositoryImpl(JpaRolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }


    @Override
    public RolePermission save(RolePermission rolePermission) {
        return rolePermissionRepository.save(rolePermission);
    }
    @Override
    public List<RolePermission> findByRoleId(String id) {
        return rolePermissionRepository.findByRoleId(id);
    }

    @Override
    public Optional<RolePermission> findById(String id) {
        return rolePermissionRepository.findById(id);
    }


}
