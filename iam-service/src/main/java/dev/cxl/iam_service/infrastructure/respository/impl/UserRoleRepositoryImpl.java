package dev.cxl.iam_service.infrastructure.respository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.mapper.UserRoleMapper;
import dev.cxl.iam_service.domain.domainentity.UserRole;
import dev.cxl.iam_service.domain.repository.UserRoleRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.UserRoleEntity;
import dev.cxl.iam_service.infrastructure.persistent.JpaUserRoleRepository;

@Component
public class UserRoleRepositoryImpl implements UserRoleRepositoryDomain {
    private final JpaUserRoleRepository userRoleRepository;
    private final UserRoleMapper userRoleMapper;

    public UserRoleRepositoryImpl(JpaUserRoleRepository userRoleRepository, UserRoleMapper userRoleMapper) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public Optional<UserRole> findById(String id) {
        UserRoleEntity userRoleEntity =
                userRoleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_ROLE_NOT_EXISTED));
        UserRole userRole = userRoleMapper.toUserRole(userRoleEntity);
        return Optional.of(userRole);
    }

    @Override
    public List<UserRole> findAllByIds(List<String> strings) {
        return List.of();
    }

    @Override
    public List<UserRole> findByUserID(String userId) {
        return userRoleMapper.toUserRoleDomain(userRoleRepository.findByUserID(userId));
    }

    @Override
    public UserRole save(UserRole userRole) {
        return userRoleMapper.toUserRole(userRoleRepository.save(userRoleMapper.toUserRole(userRole)));
    }

    @Override
    public boolean saveAll(List<UserRole> userRoles) {
        userRoleMapper.toUserRoles(userRoles);
        userRoleRepository.saveAll(userRoleMapper.toUserRoles(userRoles));
        return true;
    }

    @Override
    public boolean existsByUserIdAndRoleId(String userId, String roleId) {
        return !userRoleRepository.existsUserRoleByUserIDAndRoleID(userId, roleId);
    }

    @Override
    public boolean delete(UserRole domain) {
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
}
