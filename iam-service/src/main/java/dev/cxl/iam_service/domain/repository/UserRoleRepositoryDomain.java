package dev.cxl.iam_service.domain.repository;

import java.util.List;
import java.util.Optional;

import dev.cxl.iam_service.infrastructure.entity.UserRoleEntity;

public interface UserRoleRepositoryDomain {
    Optional<UserRoleEntity> findById(String id);

    List<UserRoleEntity> findByUserID(String userId);

    UserRoleEntity save(UserRoleEntity userRole);

    List<UserRoleEntity> saveAll(List<UserRoleEntity> userRoles);

    boolean existsByUserIdAndRoleId(String userId, String roleId);
}
