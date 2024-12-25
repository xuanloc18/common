package dev.cxl.iam_service.domain.repository;
import dev.cxl.iam_service.infrastructure.entity.UserRole;
import java.util.List;
import java.util.Optional;

public interface UserRoleRepository  {
    Optional<UserRole> findById(String id);
    List<UserRole> findByUserID(String userId);
    UserRole save(UserRole userRole);
}
