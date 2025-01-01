package dev.cxl.iam_service.infrastructure.respository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public Role save(Role role) {
        if (role.getRolePermission() != null && !role.getRolePermission().isEmpty()) {
            List<RolePermissionEntity> rolePermissionEntity =
                    rolePermissionMapper.toRolePermissionEntities(role.getRolePermission());
            rolePermissionRepository.saveAll(rolePermissionEntity);
        }
        RoleEntity roleEntity = roleMapper.toRole(role);
        return roleMapper.toRoleDomain(jpaRoleRepository.save(roleEntity));
    }

    @Override
    public Optional<Role> findById(String id) {
        RoleEntity roleEntity =
                jpaRoleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        return Optional.of(roleMapper.toRoleDomain(roleEntity));
    }

    @Override
    public Optional<Role> findByCode(String code) {
        RoleEntity roleEntity =
                jpaRoleRepository.findByCode(code).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        Role role = roleMapper.toRoleDomain(roleEntity);
        List<RolePermission> rolePermissions =
                rolePermissionMapper.toRolePermissions(rolePermissionRepository.findByRoleId(roleEntity.getId()));
        role.setRolePermissions(rolePermissions);
        return Optional.of(role);
    }

    @Override
    public boolean existsById(String id) {
        return jpaRoleRepository.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRoleRepository.existsByCode(code);
    }

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return mapToRolePage(jpaRoleRepository.findAll(pageable));
    }

    @Override
    public List<Role> findAllByIds(List<String> strings) {

        return roleMapper.toRoles(jpaRoleRepository.findAllById(strings));
    }

    @Override
    public List<Role> findAllByCodeIn(List<String> strings) {
        return roleMapper.toRoles(jpaRoleRepository.findByCodeIn(strings));
    }

    @Override
    public boolean saveAll(List<Role> domains) {
        return false;
    }

    @Override
    public boolean delete(Role domain) {
        return false;
    }

    @Override
    public boolean deleteById(String s) {
        return false;
    }

    public Page<Role> mapToRolePage(Page<RoleEntity> roleEntityPage) {
        List<Role> roles = roleEntityPage.getContent().stream()
                .map(roleMapper::toRoleDomain) // Chuyển đổi từng `RoleEntity` thành `Role`
                .toList();
        return new PageImpl<>(roles, roleEntityPage.getPageable(), roleEntityPage.getTotalElements());
    }
}
