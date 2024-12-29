package dev.cxl.iam_service.infrastructure.persistent;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.cxl.iam_service.infrastructure.entity.RoleEntity;

@Repository
public interface JpaRoleRepository extends JpaRepository<RoleEntity, String> {
    Boolean existsByCode(String code);

    Page<RoleEntity> findAll(Pageable pageable);

    Optional<RoleEntity> findByCode(String code);
}
