package dev.cxl.iam_service.infrastructure.respository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import dev.cxl.iam_service.domain.repository.UserRoleRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.UserRoleEntity;
import dev.cxl.iam_service.infrastructure.persistent.JpaUserRoleRepository;

@Component
public class UserRoleRepositoryImpl implements UserRoleRepositoryDomain {
    private final JpaUserRoleRepository userRoleRepository;

    public UserRoleRepositoryImpl(JpaUserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Optional<UserRoleEntity> findById(String id) {
        return userRoleRepository.findById(id);
    }

    @Override
    public List<UserRoleEntity> findByUserID(String userId) {
        return userRoleRepository.findByUserID(userId);
    }

    @Override
    public UserRoleEntity save(UserRoleEntity userRole) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public List<UserRoleEntity> saveAll(List<UserRoleEntity> userRoles) {
        return userRoleRepository.saveAll(userRoles);
    }

    @Override
    public boolean existsByUserIdAndRoleId(String userId, String roleId) {
        return userRoleRepository.existsUserRoleByUserIDAndRoleID(userId, roleId);
    }
}
