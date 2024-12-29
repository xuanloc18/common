package dev.cxl.iam_service.infrastructure.persistent;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.cxl.iam_service.infrastructure.entity.PermissionEntity;

@Repository
public interface JpaPermissionRespository extends JpaRepository<PermissionEntity, String> {
    Page<PermissionEntity> findAll(Pageable pageable);

    Boolean existsByResourceCodeAndScope(String resourceCode, String scope);

    Optional<PermissionEntity> findByResourceCodeAndScope(String resourceCode, String scope);

    @Query("SELECT p.id FROM PermissionEntity p "
            + "JOIN RolePermissionEntity rp ON p.id = rp.permissionId "
            + "JOIN UserRoleEntity ur ON rp.roleId = ur.roleID "
            + "JOIN RoleEntity r ON r.id = ur.roleID "
            + "JOIN UserEntity u ON ur.userID = u.userID "
            + "WHERE u.userID = :userID "
            + "AND p.deleted = false "
            + "AND ur.deleted = false "
            + "AND r.deleted = false "
            + "AND rp.deleted = false "
            + "AND p.resourceCode = :resourceCode "
            + "AND p.scope = :scope ")
    Optional<String> findPermissionIdByUserAndScope(
            @Param("userID") String userID, @Param("resourceCode") String resourceCode, @Param("scope") String scope);

    @Query("SELECT p FROM PermissionEntity p "
            + "JOIN RolePermissionEntity rp ON p.id = rp.permissionId "
            + "JOIN UserRoleEntity ur ON rp.roleId = ur.roleID "
            + "JOIN RoleEntity r ON r.id = ur.roleID "
            + "JOIN UserEntity u ON ur.userID = u.userID "
            + "WHERE u.userID = :userID "
            + "AND p.deleted =false "
            + "AND ur.deleted = false "
            + "AND r.deleted = false "
            + "AND rp.deleted = false ")
    List<PermissionEntity> findPermissionIdByUser(@Param("userID") String userID);
}
