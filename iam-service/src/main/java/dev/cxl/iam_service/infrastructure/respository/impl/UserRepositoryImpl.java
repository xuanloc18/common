package dev.cxl.iam_service.infrastructure.respository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.mapper.UserMapper;
import dev.cxl.iam_service.application.mapper.UserRoleMapper;
import dev.cxl.iam_service.domain.domainentity.User;
import dev.cxl.iam_service.domain.domainentity.UserRole;
import dev.cxl.iam_service.domain.repository.UserRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.UserEntity;
import dev.cxl.iam_service.infrastructure.entity.UserRoleEntity;
import dev.cxl.iam_service.infrastructure.persistent.JpaUserRepository;
import dev.cxl.iam_service.infrastructure.persistent.JpaUserRoleRepository;

@Component
public class UserRepositoryImpl implements UserRepositoryDomain {
    private final JpaUserRepository jpaUserRepository;
    private final JpaUserRoleRepository jpaUserRoleRepository;
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    public UserRepositoryImpl(
            JpaUserRepository jpaUserRepository,
            JpaUserRoleRepository jpaUserRoleRepository,
            UserMapper userMapper,
            UserRoleMapper userRoleMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.jpaUserRoleRepository = jpaUserRoleRepository;
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public User getUserByEmail(String email) {
        UserEntity userEntity =
                jpaUserRepository.findByUserMail(email).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        User user = userMapper.toUserDomain(userEntity);
        List<UserRoleEntity> userRoleEntity = jpaUserRoleRepository.findByUserID(user.getUserID());
        user.setUserRoles(userRoleMapper.toUserRoleDomain(userRoleEntity));
        return user;
    }

    @Override
    public User getUserByUserId(String userId) {
        UserEntity userEntity =
                jpaUserRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        User user = userMapper.toUserDomain(userEntity);
        List<UserRoleEntity> userRoleEntity = jpaUserRoleRepository.findByUserID(user.getUserID());
        if (userRoleEntity != null && !userRoleEntity.isEmpty()) {
            List<UserRole> userRoleDomains = userRoleMapper.toUserRoleDomain(userRoleEntity);
            user.setUserRoles(userRoleDomains);
        }
        return user;
    }

    @Override
    public UserEntity save(User user) {
        UserEntity userEntity = userMapper.toUserEntity(user);
        List<UserRoleEntity> userRole = userRoleMapper.toUserRoles(user.getUserRoles());
        jpaUserRoleRepository.saveAll(userRole);
        return jpaUserRepository.save(userEntity);
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


}
