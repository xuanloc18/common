package dev.cxl.iam_service.infrastructure.respository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.mapper.UserMapper;
import dev.cxl.iam_service.application.mapper.UserRoleMapper;
import dev.cxl.iam_service.domain.domainentity.User;
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
    public List<User> findAllByIds(List<String> strings) {
        return List.of();
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userMapper.toUserEntity(user);
        List<UserRoleEntity> userRole = userRoleMapper.toUserRoles(user.getUserRoles());
        jpaUserRoleRepository.saveAll(userRole);
        return userMapper.toUserDomain(jpaUserRepository.save(userEntity));
    }

    @Override
    public boolean saveAll(List<User> domains) {
        return false;
    }

    @Override
    public boolean delete(User domain) {
        return false;
    }

    @Override
    public boolean deleteById(String s) {
        return false;
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Optional<User> findById(String id) {
        UserEntity userEntity =
                jpaUserRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        User user = userMapper.toUserDomain(userEntity);
        List<UserRoleEntity> userRoleEntity = jpaUserRoleRepository.findByUserID(user.getUserID());
        user.setuserRoles(userRoleMapper.toUserRoleDomain(userRoleEntity));
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByEmail(String userMail) {
        UserEntity userEntity =
                jpaUserRepository.findByUserMail(userMail).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        User user = userMapper.toUserDomain(userEntity);
        List<UserRoleEntity> userRoleEntity = jpaUserRoleRepository.findByUserID(user.getUserID());
        user.setuserRoles(userRoleMapper.toUserRoleDomain(userRoleEntity));
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByUserKCLID(String id) {
        UserEntity userEntity =
                jpaUserRepository.findByUserKCLID(id).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        User user = userMapper.toUserDomain(userEntity);
        List<UserRoleEntity> userRoleEntity = jpaUserRoleRepository.findByUserID(user.getUserID());
        user.setuserRoles(userRoleMapper.toUserRoleDomain(userRoleEntity));
        return Optional.of(user);
    }

    @Override
    public boolean existsByUserMail(String userMail) {
        return jpaUserRepository.existsByUserMail(userMail);
    }
}
