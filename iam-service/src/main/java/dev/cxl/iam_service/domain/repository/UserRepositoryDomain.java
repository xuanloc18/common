package dev.cxl.iam_service.domain.repository;

import java.util.Optional;

import com.evo.common.DomainRepository;

import dev.cxl.iam_service.domain.domainentity.User;

public interface UserRepositoryDomain extends DomainRepository<User, String> {

    User save(User user);

    Optional<User> findById(String id);

    Optional<User> findByEmail(String userMail);

    Optional<User> findByUserKCLID(String string);

    boolean existsByUserMail(String userMail);
}
