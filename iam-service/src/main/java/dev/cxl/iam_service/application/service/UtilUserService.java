package dev.cxl.iam_service.application.service;

import dev.cxl.iam_service.application.mapper.UserMapper;
import dev.cxl.iam_service.application.mapper.UserRoleMapper;
import dev.cxl.iam_service.domain.domainentity.UserDomain;
import dev.cxl.iam_service.domain.domainentity.UserRoleDomain;
import dev.cxl.iam_service.domain.repository.UserRoleRepository;
import dev.cxl.iam_service.infrastructure.entity.UserRole;
import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.domain.repository.UserInformationRepository;
import dev.cxl.iam_service.domain.repository.UserRepository;
import dev.cxl.iam_service.infrastructure.entity.User;

import java.util.List;

@Service
public class UtilUserService {

    private final UserRepository userRepository;

    private final UserInformationRepository userInformationRepository;

    private final UserMapper userMapper;

    private final UserRoleMapper userRoleMapper;

    private final UserRoleRepository userRoleRepository;

    public UtilUserService(UserRepository userRepository, UserInformationRepository userInformationRepository, UserMapper userMapper, UserRoleMapper userRoleMapper, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userInformationRepository = userInformationRepository;
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
        this.userRoleRepository = userRoleRepository;
    }

    public User finUserId(String id) {
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public User finUserKCLId(String kCLSID) {
        return userRepository.findByUserKCLID(kCLSID).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public User finUserMail(String userMail) {
        return userRepository.findByUserMail(userMail).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public boolean userExists(String userName) {
        return userInformationRepository.existsUserInformationByUsername(userName);
    }
    public UserDomain getUserDomain(String userID) {
        User user = finUserId(userID);
        UserDomain userDomain = userMapper.toUserDomain(user);
        List<UserRole> userRole= userRoleRepository.findByUserID(userID);
        List<UserRoleDomain> userRoleDomains=userRoleMapper.toUserRoleDomain(userRole);
        userDomain.setUserRoles(userRoleDomains);
        return userDomain;
    }
}
