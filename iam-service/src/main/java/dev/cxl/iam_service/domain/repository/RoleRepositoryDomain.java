package dev.cxl.iam_service.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.cxl.iam_service.infrastructure.entity.RoleEntity;

public interface RoleRepositoryDomain {
    RoleEntity save(RoleEntity role);

    Boolean existsByCode(String code);

    Page<RoleEntity> findAll(Pageable pageable);

    List<RoleEntity> findAll(List<String> strings);
    List<RoleEntity> findAllByCodeIn(List<String> strings);

    Optional<RoleEntity> findByCode(String code);
}
