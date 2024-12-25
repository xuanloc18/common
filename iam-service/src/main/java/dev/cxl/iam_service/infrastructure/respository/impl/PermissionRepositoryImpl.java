package dev.cxl.iam_service.infrastructure.respository.impl;

import dev.cxl.iam_service.domain.repository.PermissionRepository;
import dev.cxl.iam_service.infrastructure.entity.Permission;
import dev.cxl.iam_service.infrastructure.persistent.JpaPermissionRespository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PermissionRepositoryImpl implements PermissionRepository {
    private final JpaPermissionRespository jpaPermissionRespository;

    public PermissionRepositoryImpl(JpaPermissionRespository jpaPermissionRespository) {
        this.jpaPermissionRespository = jpaPermissionRespository;
    }

    @Override
    public Permission save(Permission permission) {
        return jpaPermissionRespository.save(permission);
    }

    @Override
    public Page<Permission> findAll(Pageable pageable) {
        return jpaPermissionRespository.findAll(pageable);
    }

    @Override
    public Boolean existsByResourceCodeAndScope(String resourceCode, String scope) {
        return jpaPermissionRespository.existsByResourceCodeAndScope(resourceCode, scope);
    }

    @Override
    public Optional<Permission> findByResourceCodeAndScope(String resourceCode, String scope) {
        return jpaPermissionRespository.findByResourceCodeAndScope(resourceCode, scope);
    }

    @Override
    public Optional<String> findPermissionIdByUserAndScope(String userID, String resourceCode, String scope) {
        return jpaPermissionRespository.findPermissionIdByUserAndScope(userID, resourceCode, scope);
    }

    @Override
    public List<Permission> findPermissionIdByUser(String userID) {
        return jpaPermissionRespository.findPermissionIdByUser(userID);
    }

    @Override
    public Optional<Permission> findById(String id) {
        return jpaPermissionRespository.findById(id);
    }
}
