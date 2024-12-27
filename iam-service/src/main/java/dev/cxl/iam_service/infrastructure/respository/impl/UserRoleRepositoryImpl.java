package dev.cxl.iam_service.infrastructure.respository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import dev.cxl.iam_service.domain.repository.UserRoleRepository;
import dev.cxl.iam_service.infrastructure.entity.UserRole;
import dev.cxl.iam_service.infrastructure.persistent.JpaUserRoleRepository;

@Component
public class UserRoleRepositoryImpl implements UserRoleRepository {
    private final JpaUserRoleRepository userRoleRepository;

    public UserRoleRepositoryImpl(JpaUserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Optional<UserRole> findById(String id) {
        return userRoleRepository.findById(id);
    }

    @Override
    public List<UserRole> findByUserID(String userId) {
        return userRoleRepository.findByUserID(userId);
    }

    @Override
    public UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public List<UserRole> saveAll(List<UserRole> userRoles) {
        return userRoleRepository.saveAll(userRoles);
    }

    @Override
    public boolean existsByUserIdAndRoleId(String userId, String roleId) {
        return userRoleRepository.existsUserRoleByUserIDAndRoleID(userId,roleId);
    }
}
