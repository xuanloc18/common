package dev.cxl.iam_service.infrastructure.respository.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import dev.cxl.iam_service.domain.repository.UserRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.UserEntity;
import dev.cxl.iam_service.infrastructure.persistent.JpaUserRepository;

@Component
public class UserRepositoryImpl implements UserRepositoryDomain {
    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public UserEntity save(UserEntity user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public Optional<UserEntity> findById(String id) {
        return jpaUserRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUserMail(String userMail) {
        return jpaUserRepository.findByUserMail(userMail);
    }

    @Override
    public Optional<UserEntity> findByUserKCLID(String string) {
        return jpaUserRepository.findByUserKCLID(string);
    }

    @Override
    public boolean existsByUserMail(String userMail) {
        return jpaUserRepository.existsByUserMail(userMail);
    }

    @Override
    public Page<UserEntity> findAll(Pageable pageable) {
        return jpaUserRepository.findAll(pageable);
    }

    @Override
    public Page<UserEntity> findUsersByKey(String key, Pageable pageable) {
        return jpaUserRepository.findUsersByKey(key, pageable);
    }
}
