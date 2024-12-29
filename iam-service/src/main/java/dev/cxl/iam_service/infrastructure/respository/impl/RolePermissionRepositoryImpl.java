package dev.cxl.iam_service.infrastructure.respository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import dev.cxl.iam_service.domain.repository.RolePermissionRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.RolePermissionEntity;
import dev.cxl.iam_service.infrastructure.persistent.JpaRolePermissionRepository;

@Component
public class RolePermissionRepositoryImpl implements RolePermissionRepositoryDomain {
    private final JpaRolePermissionRepository rolePermissionRepository;

    public RolePermissionRepositoryImpl(JpaRolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public RolePermissionEntity save(RolePermissionEntity rolePermission) {
        return rolePermissionRepository.save(rolePermission);
    }

    @Override
    public List<RolePermissionEntity> findByRoleId(String id) {
        return rolePermissionRepository.findByRoleId(id);
    }

    @Override
    public Optional<RolePermissionEntity> findById(String id) {
        return rolePermissionRepository.findById(id);
    }

    @Override
    public List<RolePermissionEntity> saveAll(List<RolePermissionEntity> rolePermissions) {
        return rolePermissionRepository.saveAll(rolePermissions);
    }
}
