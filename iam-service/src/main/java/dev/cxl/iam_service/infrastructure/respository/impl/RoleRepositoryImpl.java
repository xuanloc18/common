package dev.cxl.iam_service.infrastructure.respository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import dev.cxl.iam_service.domain.repository.RoleRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.RoleEntity;
import dev.cxl.iam_service.infrastructure.persistent.JpaRoleRepository;

@Component
public class RoleRepositoryImpl implements RoleRepositoryDomain {
    private final JpaRoleRepository jpaRoleRepository;

    public RoleRepositoryImpl(JpaRoleRepository jpaRoleRepository) {
        this.jpaRoleRepository = jpaRoleRepository;
    }

    @Override
    public RoleEntity save(RoleEntity role) {
        return jpaRoleRepository.save(role);
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
