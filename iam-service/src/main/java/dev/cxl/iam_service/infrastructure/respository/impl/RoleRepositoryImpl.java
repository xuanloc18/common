package dev.cxl.iam_service.infrastructure.respository.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.mapper.RoleMapper;
import dev.cxl.iam_service.application.mapper.RolePermissionMapper;
import dev.cxl.iam_service.domain.domainentity.Role;
import dev.cxl.iam_service.domain.domainentity.RolePermission;
import dev.cxl.iam_service.domain.repository.RoleRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.RoleEntity;
import dev.cxl.iam_service.infrastructure.entity.RolePermissionEntity;
import dev.cxl.iam_service.infrastructure.persistent.JpaRolePermissionRepository;
import dev.cxl.iam_service.infrastructure.persistent.JpaRoleRepository;

@Component
public class RoleRepositoryImpl implements RoleRepositoryDomain {
    private final JpaRoleRepository jpaRoleRepository;
    private final JpaRolePermissionRepository rolePermissionRepository;
    private final RolePermissionMapper rolePermissionMapper;
    private final RoleMapper roleMapper;

    public RoleRepositoryImpl(
            JpaRoleRepository jpaRoleRepository,
            JpaRolePermissionRepository rolePermissionRepository,
            RolePermissionMapper rolePermissionMapper,
            RoleMapper roleMapper) {
        this.jpaRoleRepository = jpaRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.rolePermissionMapper = rolePermissionMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public RoleEntity save(Role role) {
        if (role.getRolePermissionDomains() != null
                && !role.getRolePermissionDomains().isEmpty()) {
            List<RolePermissionEntity> rolePermissionEntity =
                    rolePermissionMapper.toRolePermissions(role.getRolePermissionDomains());
            rolePermissionRepository.saveAll(rolePermissionEntity);
        }
        RoleEntity roleEntity = roleMapper.toRole(role);
        return jpaRoleRepository.save(roleEntity);
    }

    @Override
    public void saveAfterDeletePer(Role role) {
        Role roleData = getRoleByCode(role.getCode());
        List<RolePermission> listUserRoleData = roleData.getRolePermissionDomains();
        List<RolePermission> listUserRoleAfterDelete = role.getRolePermissionDomains();
        List<RolePermission> notIn = listUserRoleData.stream()
                .filter(rolePermission -> listUserRoleAfterDelete.stream()
                        .noneMatch(rolePermission1 -> rolePermission1.getId().equals(rolePermission.getId())))
                .collect(Collectors.toList());
        notIn.forEach(rolePermission -> {
            rolePermission.delete();
        });
        rolePermissionRepository.saveAll(rolePermissionMapper.toRolePermissions(notIn));
    }

    @Override
    public Role findById(String id) {
        RoleEntity roleEntity =
                jpaRoleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        return roleMapper.toRoleDomain(roleEntity);
    }

    @Override
    public void saveAfterAddPer(Role role) {
        Role roleData = getRoleByCode(role.getCode());
        List<RolePermission> listUserRoleData = roleData.getRolePermissionDomains();
        List<RolePermission> listUserRoleAfterAdd = role.getRolePermissionDomains();

        List<RolePermission> notIn = listUserRoleAfterAdd.stream()
                .filter(rolePermission -> listUserRoleData.stream()
                        .noneMatch(
                                rolePermission1 ->
                                        rolePermission.getRoleId().equalsIgnoreCase(rolePermission1.getRoleId())
                                                && rolePermission
                                                        .getPermissionId()
                                                        .equalsIgnoreCase(
                                                                rolePermission1.getPermissionId()) // Sửa logic so sánh
                                ))
                .collect(Collectors.toList());
        rolePermissionRepository.saveAll(rolePermissionMapper.toRolePermissions(notIn));
    }

    @Override
    public Role getRoleByCode(String code) {
        RoleEntity roleEntity =
                jpaRoleRepository.findByCode(code).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        Role role = roleMapper.toRoleDomain(roleEntity);
        List<RolePermission> rolePermissions =
                rolePermissionMapper.toRolePermissionDomains(rolePermissionRepository.findByRoleId(roleEntity.getId()));
        role.setRolePermissions(rolePermissions);
        return role;
    }

    @Override
    public Boolean existsByCode(String code) {
        return jpaRoleRepository.existsByCode(code);
    }

    @Override
    public Page<RoleEntity> findAll(Pageable pageable) {
        return jpaRoleRepository.findAll(pageable);
    }

    @Override
    public List<RoleEntity> findAll(List<String> strings) {
        return jpaRoleRepository.findAllById(strings);
    }

    @Override
    public List<RoleEntity> findAllByCodeIn(List<String> strings) {
        return jpaRoleRepository.findByCodeIn(strings);
    }

    @Override
    public Optional<RoleEntity> findByCode(String code) {
        return jpaRoleRepository.findByCode(code);
    }
}
