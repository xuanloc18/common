package dev.cxl.iam_service.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.cxl.iam_service.domain.domainentity.User;
import dev.cxl.iam_service.infrastructure.entity.UserEntity;

public interface UserRepositoryDomain {
    User getUserByEmail(String email);

    User getUserByUserId(String userId);

    void saveAfterDeleteRole(User user);

    void saveAfterAddRole(User user);

    UserEntity save(User user);

    Optional<UserEntity> findById(String id);

    Optional<UserEntity> findByUserMail(String userMail);

    Optional<UserEntity> findByUserKCLID(String string);

    boolean existsByUserMail(String userMail);

    Page<UserEntity> findAll(Pageable pageable);
}
