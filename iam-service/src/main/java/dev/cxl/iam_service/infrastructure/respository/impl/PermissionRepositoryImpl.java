package dev.cxl.iam_service.infrastructure.respository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.mapper.PermissionMapper;
import dev.cxl.iam_service.domain.domainentity.Permission;
import dev.cxl.iam_service.domain.repository.PermissionRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.PermissionEntity;
import dev.cxl.iam_service.infrastructure.persistent.JpaPermissionRespository;

@Component
public class PermissionRepositoryImpl implements PermissionRepositoryDomain {
    private final PermissionMapper permissionMapper;
    private final JpaPermissionRespository jpaPermissionRespository;

    public PermissionRepositoryImpl(
            PermissionMapper permissionMapper, JpaPermissionRespository jpaPermissionRespository) {
        this.permissionMapper = permissionMapper;
        this.jpaPermissionRespository = jpaPermissionRespository;
    }

    @Override
    public Permission save(Permission permission) {
        return permissionMapper.toPermission(
                jpaPermissionRespository.save(permissionMapper.toPermissionEntity(permission)));
    }

    @Override
    public boolean saveAll(List<Permission> domains) {
        jpaPermissionRespository.saveAll(permissionMapper.toPermissionEntities(domains));
        return true;
    }

    @Override
    public boolean delete(Permission domain) {
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

    @Override
    public Page<Permission> findAll(Pageable pageable) {

        return mapToRolePage(jpaPermissionRespository.findAll(pageable));
    }

    @Override
    public Boolean existsByResourceCodeAndScope(String resourceCode, String scope) {
        return jpaPermissionRespository.existsByResourceCodeAndScope(resourceCode, scope);
    }

    @Override
    public List<Permission> findAllByIds(List<String> ids) {
        return permissionMapper.toPermissions(jpaPermissionRespository.findAllById(ids));
    }

    @Override
    public List<Permission> findPermissionIdByUser(String userID) {
        return permissionMapper.toPermissions(jpaPermissionRespository.findPermissionIdByUser(userID));
    }

    @Override
    public Optional<Permission> findById(String id) {
        PermissionEntity permissionEntity = jpaPermissionRespository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_PERMISSION_NOT_EXISTED));
        return Optional.of(permissionMapper.toPermission(permissionEntity));
    }

    public Page<Permission> mapToRolePage(Page<PermissionEntity> permissionEntities) {
        List<Permission> roles = permissionEntities.getContent().stream()
                .map(permissionMapper::toPermission) // Chuyển đổi từng `RoleEntity` thành `Role`
                .toList();
        return new PageImpl<>(roles, permissionEntities.getPageable(), permissionEntities.getTotalElements());
    }
}
