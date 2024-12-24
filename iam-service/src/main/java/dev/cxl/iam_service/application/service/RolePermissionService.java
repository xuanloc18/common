package dev.cxl.iam_service.application.service;

import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.domain.entity.Permission;
import dev.cxl.iam_service.domain.entity.Role;
import dev.cxl.iam_service.domain.entity.RolePermission;
import dev.cxl.iam_service.infrastructure.respository.PermissionRespository;
import dev.cxl.iam_service.infrastructure.respository.RolePermissionRepository;
import dev.cxl.iam_service.infrastructure.respository.RoleRepository;

@Service
public class RolePermissionService {

    private final RoleRepository roleRepository;

    private final PermissionRespository permissionRespository;

    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionService(
            RoleRepository roleRepository,
            PermissionRespository permissionRespository,
            RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRespository = permissionRespository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public RolePermission create(String roleCode, String perRe, String perCode) {
        Role role = roleRepository.findByCode(roleCode).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        Permission permission = permissionRespository
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