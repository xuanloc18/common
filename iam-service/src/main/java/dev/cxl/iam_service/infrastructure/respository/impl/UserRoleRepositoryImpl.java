package dev.cxl.iam_service.infrastructure.respository.impl;

import dev.cxl.iam_service.domain.repository.UserRoleRepository;
import dev.cxl.iam_service.infrastructure.entity.UserRole;
import dev.cxl.iam_service.infrastructure.persistent.JpaUserRoleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
}
