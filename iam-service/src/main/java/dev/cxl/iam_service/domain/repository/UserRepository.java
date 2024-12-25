package dev.cxl.iam_service.domain.repository;

import dev.cxl.iam_service.infrastructure.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface UserRepository {

    User save(User user);
    Optional<User> findById(String id);
    Optional<User> findByUserMail(String userMail);

    Optional<User> findByUserKCLID(String string);

    boolean existsByUserMail(String userMail);

    Page<User> findAll(Pageable pageable);

    Page<User> findUsersByKey(String key, Pageable pageable);
}