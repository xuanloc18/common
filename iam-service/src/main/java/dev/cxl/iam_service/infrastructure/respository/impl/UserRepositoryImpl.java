package dev.cxl.iam_service.infrastructure.respository.impl;

import dev.cxl.iam_service.domain.repository.UserRepository;
import dev.cxl.iam_service.infrastructure.entity.User;
import dev.cxl.iam_service.infrastructure.persistent.JpaUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }
    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return jpaUserRepository.findById(id);
    }


    @Override
    public Optional<User> findByUserMail(String userMail) {
       return jpaUserRepository.findByUserMail(userMail);
    }

    @Override
    public Optional<User> findByUserKCLID(String string) {
        return  jpaUserRepository.findByUserKCLID(string);
    }

    @Override
    public boolean existsByUserMail(String userMail) {
        return jpaUserRepository.existsByUserMail(userMail);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return jpaUserRepository.findAll(pageable);
    }

    @Override
    public Page<User> findUsersByKey(String key, Pageable pageable) {
        return jpaUserRepository.findUsersByKey(key, pageable);
    }
}
