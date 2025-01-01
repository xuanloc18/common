package dev.cxl.iam_service.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.evo.common.DomainRepository;

import dev.cxl.iam_service.domain.domainentity.Role;

public interface RoleRepositoryDomain extends DomainRepository<Role, String> {
    Role save(Role role);

    Optional<Role> findById(String id);

    Optional<Role> findByCode(String code);

    boolean existsByCode(String code);

    Page<Role> findAll(Pageable pageable);

    List<Role> findAllByIds(List<String> strings);

    List<Role> findAllByCodeIn(List<String> strings);

    boolean existsById(String id);
}
