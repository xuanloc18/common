package dev.cxl.iam_service.application.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.mapper.UserMapper;
import dev.cxl.iam_service.application.mapper.UserRoleMapper;
import dev.cxl.iam_service.application.service.custom.UtilUserService;
import dev.cxl.iam_service.domain.domainentity.User;
import dev.cxl.iam_service.domain.domainentity.UserRole;
import dev.cxl.iam_service.domain.repository.UserInformationRepositoryDomain;
import dev.cxl.iam_service.domain.repository.UserRepositoryDomain;
import dev.cxl.iam_service.domain.repository.UserRoleRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.UserEntity;
import dev.cxl.iam_service.infrastructure.entity.UserRoleEntity;

@Service
public class UtilUserServiceImpl implements UtilUserService {

    private final UserRepositoryDomain userRepository;

    private final UserInformationRepositoryDomain userInformationRepository;

    private final UserMapper userMapper;

    private final UserRoleMapper userRoleMapper;

    private final UserRoleRepositoryDomain userRoleRepository;

    public UtilUserServiceImpl(
            UserRepositoryDomain userRepository,
            UserInformationRepositoryDomain userInformationRepository,
            UserMapper userMapper,
            UserRoleMapper userRoleMapper,
            UserRoleRepositoryDomain userRoleRepository) {
        this.userRepository = userRepository;
        this.userInformationRepository = userInformationRepository;
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserEntity finUserId(String id) {
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public UserEntity finUserKCLId(String kCLSID) {
        return userRepository.findByUserKCLID(kCLSID).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public UserEntity finUserMail(String userMail) {
        return userRepository.findByUserMail(userMail).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public boolean userExists(String userName) {
        return userInformationRepository.existsUserInformationByUsername(userName);
    }

    @Override
    public User getUserDomain(String userID) {
        UserEntity user = finUserId(userID);
        User userDomain = userMapper.toUserDomain(user);
        List<UserRoleEntity> userRole = userRoleRepository.findByUserID(userID);
        List<UserRole> userRoleDomains = userRoleMapper.toUserRoleDomain(userRole);
        userDomain.setUserRoles(userRoleDomains);
        return userDomain;
    }
}
