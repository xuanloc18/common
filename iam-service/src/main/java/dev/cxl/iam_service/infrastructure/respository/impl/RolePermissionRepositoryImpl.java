package dev.cxl.iam_service.infrastructure.respository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.mapper.RolePermissionMapper;
import dev.cxl.iam_service.domain.domainentity.RolePermission;
import dev.cxl.iam_service.domain.repository.RolePermissionRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.RolePermissionEntity;
import dev.cxl.iam_service.infrastructure.persistent.JpaRolePermissionRepository;

@Component
public class RolePermissionRepositoryImpl implements RolePermissionRepositoryDomain {
    private final JpaRolePermissionRepository rolePermissionRepository;
    private final RolePermissionMapper rolePermissionMapper;

    public RolePermissionRepositoryImpl(
            JpaRolePermissionRepository rolePermissionRepository, RolePermissionMapper rolePermissionMapper) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.rolePermissionMapper = rolePermissionMapper;
    }

    @Override
    public RolePermission save(RolePermission rolePermission) {
        return rolePermissionMapper.toRolePermission(
                rolePermissionRepository.save(rolePermissionMapper.toRolePermissionEntity(rolePermission)));
    }

    @Override
    public List<RolePermission> findByRoleId(String id) {
        List<RolePermissionEntity> rolePermissionEntities = rolePermissionRepository.findByRoleId(id);
        return rolePermissionMapper.toRolePermissions(rolePermissionEntities);
    }

    @Override
    public Optional<RolePermission> findById(String id) {
        RolePermissionEntity rolePermissionEntity = rolePermissionRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_PERMISSION_NOT_EXISTED));
        return Optional.of(rolePermissionMapper.toRolePermission(rolePermissionEntity));
    }

    @Override
    public List<RolePermission> findAllByIds(List<String> strings) {
        return List.of();
    }

    @Override
    public boolean saveAll(List<RolePermission> rolePermissions) {
        rolePermissionMapper.toRolePermissionEntities(rolePermissions);
        return true;
    }

    @Override
    public boolean delete(RolePermission domain) {
        return false;
    }

    @Override
    public boolean deleteById(String s) {
        return false;
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }
}
