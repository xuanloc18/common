package dev.cxl.iam_service.application.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.domain.repository.PermissionRepositoryDomain;
import dev.cxl.iam_service.domain.repository.RolePermissionRepositoryDomain;
import dev.cxl.iam_service.domain.repository.RoleRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.PermissionEntity;
import dev.cxl.iam_service.infrastructure.entity.RoleEntity;
import dev.cxl.iam_service.infrastructure.entity.RolePermissionEntity;

@Service
public class RolePermissionService {

    private final RoleRepositoryDomain roleRepository;

    private final PermissionRepositoryDomain permissionRepository;

    private final RolePermissionRepositoryDomain rolePermissionRepository;

    public RolePermissionService(
            RoleRepositoryDomain roleRepository,
            PermissionRepositoryDomain permissionRepository,
            RolePermissionRepositoryDomain rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public RolePermissionEntity create(String roleCode, String perRe, String perCode) {
        RoleEntity role =
                roleRepository.findByCode(roleCode).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        PermissionEntity permission = permissionRepository
                .findByResourceCodeAndScope(perRe, perCode)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));
        if (permission.getDeleted()) {
            throw new AppException(ErrorCode.PERMISSION_DELETE);
        }
        Boolean check = rolePermissionRepository.findByRoleId(role.getId()).stream()
                .anyMatch(rolePermission -> rolePermission.getPermissionId().equals(permission.getId()));
        if (check) throw new AppException(ErrorCode.ROLE_HAD_PERMISSION);
        return rolePermissionRepository.save(RolePermissionEntity.builder()
                .roleId(role.getId())
                .permissionId(permission.getId())
                .deleted(false)
                .build());
    }

    public void saveAll(List<RolePermissionEntity> rolePermissions) {
        rolePermissionRepository.saveAll(rolePermissions);
    }

    public boolean delete(String id) {
        RolePermissionEntity rolePermission = rolePermissionRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_PERMISSION_NOT_EXISTED));
        rolePermission.setDeleted(true);
        rolePermissionRepository.save(rolePermission);
        return true;
    }
}
