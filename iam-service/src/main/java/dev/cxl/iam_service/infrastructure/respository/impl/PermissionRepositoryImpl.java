package dev.cxl.iam_service.infrastructure.respository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import dev.cxl.iam_service.domain.repository.PermissionRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.PermissionEntity;
import dev.cxl.iam_service.infrastructure.persistent.JpaPermissionRespository;

@Component
public class PermissionRepositoryImpl implements PermissionRepositoryDomain {
    private final JpaPermissionRespository jpaPermissionRespository;

    public PermissionRepositoryImpl(JpaPermissionRespository jpaPermissionRespository) {
        this.jpaPermissionRespository = jpaPermissionRespository;
    }

    @Override
    public PermissionEntity save(PermissionEntity permission) {
        return jpaPermissionRespository.save(permission);
    }

    @Override
    public Page<PermissionEntity> findAll(Pageable pageable) {
        return jpaPermissionRespository.findAll(pageable);
    }

    @Override
    public Boolean existsByResourceCodeAndScope(String resourceCode, String scope) {
        return jpaPermissionRespository.existsByResourceCodeAndScope(resourceCode, scope);
    }

    @Override
    public Optional<PermissionEntity> findByResourceCodeAndScope(String resourceCode, String scope) {
        return jpaPermissionRespository.findByResourceCodeAndScope(resourceCode, scope);
    }

    @Override
    public Optional<String> findPermissionIdByUserAndScope(String userID, String resourceCode, String scope) {
        return jpaPermissionRespository.findPermissionIdByUserAndScope(userID, resourceCode, scope);
    }

    @Override
    public List<PermissionEntity> findPermissionIdByUser(String userID) {
        return jpaPermissionRespository.findPermissionIdByUser(userID);
    }

    @Override
    public Optional<PermissionEntity> findById(String id) {
        return jpaPermissionRespository.findById(id);
    }
}
