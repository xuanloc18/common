package dev.cxl.iam_service.infrastructure.respository.impl;

import dev.cxl.iam_service.domain.repository.RoleRepository;
import dev.cxl.iam_service.infrastructure.entity.Role;
import dev.cxl.iam_service.infrastructure.persistent.JpaRoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class RoleRepositoryImpl implements RoleRepository {
    private final JpaRoleRepository jpaRoleRepository;

    public RoleRepositoryImpl(JpaRoleRepository jpaRoleRepository) {
        this.jpaRoleRepository = jpaRoleRepository;
    }

    @Override
    public Role save(Role role) {
        return jpaRoleRepository.save(role);
    }

    @Override
    public Boolean existsByCode(String code) {
        return jpaRoleRepository.existsByCode(code);
    }

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return jpaRoleRepository.findAll(pageable);
    }

    @Override
    public Optional<Role> findByCode(String code) {
        return jpaRoleRepository.findByCode(code);
    }
}
