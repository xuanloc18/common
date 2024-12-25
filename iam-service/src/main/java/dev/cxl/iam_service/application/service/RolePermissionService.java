package dev.cxl.iam_service.application.service;

import dev.cxl.iam_service.domain.repository.PermissionRepository;
import dev.cxl.iam_service.domain.repository.RolePermissionRepository;
import dev.cxl.iam_service.domain.repository.RoleRepository;
import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.infrastructure.entity.Permission;
import dev.cxl.iam_service.infrastructure.entity.Role;
import dev.cxl.iam_service.infrastructure.entity.RolePermission;
import dev.cxl.iam_service.infrastructure.persistent.JpaPermissionRespository;
import dev.cxl.iam_service.infrastructure.persistent.JpaRolePermissionRepository;
import dev.cxl.iam_service.infrastructure.persistent.JpaRoleRepository;

@Service
public class RolePermissionService {

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionService(RoleRepository roleRepository, PermissionRepository permissionRepository, RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }


    public RolePermission create(String roleCode, String perRe, String perCode) {
        Role role = roleRepository.findByCode(roleCode).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        Permission permission = permissionRepository
                .findByResourceCodeAndScope(perRe, perCode)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));
        if (permission.getDeleted()) {
            throw new AppException(ErrorCode.PERMISSION_DELETE);
        }
        Boolean check = rolePermissionRepository.findByRoleId(role.getId()).stream()
                .anyMatch(rolePermission -> rolePermission.getPermissionId().equals(permission.getId()));
        if (check) throw new AppException(ErrorCode.ROLE_HAD_PERMISSION);
        return rolePermissionRepository.save(RolePermission.builder()
                .roleId(role.getId())
                .permissionId(permission.getId())
                .deleted(false)
                .build());
    }

    public boolean delete(String id) {
        RolePermission rolePermission = rolePermissionRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_PERMISSION_NOT_EXISTED));
        rolePermission.setDeleted(true);
        rolePermissionRepository.save(rolePermission);
        return true;
    }
}
