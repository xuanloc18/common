package dev.cxl.iam_service.domain.repository;

import java.util.List;
import java.util.Optional;

import com.evo.common.DomainRepository;

import dev.cxl.iam_service.domain.domainentity.UserRole;

public interface UserRoleRepositoryDomain extends DomainRepository<UserRole, String> {
    Optional<UserRole> findById(String id);

    List<UserRole> findByUserID(String userId);

    UserRole save(UserRole userRole);

    boolean saveAll(List<UserRole> userRoles);

    boolean existsByUserIdAndRoleId(String userId, String roleId);
}
