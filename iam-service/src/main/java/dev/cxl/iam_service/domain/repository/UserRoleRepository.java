package dev.cxl.iam_service.domain.repository;

import java.util.List;
import java.util.Optional;

import dev.cxl.iam_service.infrastructure.entity.UserRole;

public interface UserRoleRepository {
    Optional<UserRole> findById(String id);

    List<UserRole> findByUserID(String userId);

    UserRole save(UserRole userRole);

    List<UserRole> saveAll(List<UserRole> userRoles);

    boolean existsByUserIdAndRoleId(String userId, String roleId);




}
