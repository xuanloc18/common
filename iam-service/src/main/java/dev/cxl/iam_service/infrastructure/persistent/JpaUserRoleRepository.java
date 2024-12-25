package dev.cxl.iam_service.infrastructure.persistent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.cxl.iam_service.infrastructure.entity.UserRole;

@Repository
public interface JpaUserRoleRepository extends JpaRepository<UserRole, String> {
    List<UserRole> findByUserID(String userId);
}
