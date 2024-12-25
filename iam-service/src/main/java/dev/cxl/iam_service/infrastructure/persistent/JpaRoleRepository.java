package dev.cxl.iam_service.infrastructure.persistent;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.cxl.iam_service.infrastructure.entity.Role;

@Repository
public interface JpaRoleRepository extends JpaRepository<Role, String> {
    Boolean existsByCode(String code);

    Page<Role> findAll(Pageable pageable);

    Optional<Role> findByCode(String code);

}
