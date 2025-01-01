package dev.cxl.iam_service.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.evo.common.DomainRepository;

import dev.cxl.iam_service.domain.domainentity.Permission;

public interface PermissionRepositoryDomain extends DomainRepository<Permission, String> {
    Permission save(Permission permission);

    Page<Permission> findAll(Pageable pageable);

    Boolean existsByResourceCodeAndScope(String resourceCode, String scope);

    List<Permission> findAllByIds(List<String> ids);

    List<Permission> findPermissionIdByUser(String userID);

    Optional<Permission> findById(String id);
}
