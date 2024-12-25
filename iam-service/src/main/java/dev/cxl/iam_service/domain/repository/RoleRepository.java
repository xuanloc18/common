package dev.cxl.iam_service.domain.repository;

import dev.cxl.iam_service.infrastructure.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface RoleRepository  {
    Role save(Role role);
    Boolean existsByCode(String code);

    Page<Role> findAll(Pageable pageable);

    Optional<Role> findByCode(String code);

}
