package dev.cxl.iam_service.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.cxl.iam_service.infrastructure.entity.Role;

public interface RoleRepository {
    Role save(Role role);

    Boolean existsByCode(String code);

    Page<Role> findAll(Pageable pageable);

    List<Role> findAll(List<String> strings);

    Optional<Role> findByCode(String code);
}
