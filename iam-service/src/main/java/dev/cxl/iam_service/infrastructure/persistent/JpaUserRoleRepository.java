package dev.cxl.iam_service.infrastructure.persistent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.cxl.iam_service.infrastructure.entity.UserRoleEntity;

@Repository
public interface JpaUserRoleRepository extends JpaRepository<UserRoleEntity, String> {
    List<UserRoleEntity> findByUserID(String userId);

    boolean existsUserRoleByUserIDAndRoleID(String userId, String roleId);
}
