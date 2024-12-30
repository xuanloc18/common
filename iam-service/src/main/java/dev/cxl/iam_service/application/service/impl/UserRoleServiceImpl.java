package dev.cxl.iam_service.application.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.mapper.UserRoleMapper;
import dev.cxl.iam_service.domain.domainentity.User;
import dev.cxl.iam_service.domain.domainentity.UserRole;
import dev.cxl.iam_service.domain.repository.RoleRepositoryDomain;
import dev.cxl.iam_service.domain.repository.UserRoleRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.RoleEntity;
import dev.cxl.iam_service.infrastructure.entity.UserEntity;
import dev.cxl.iam_service.infrastructure.entity.UserRoleEntity;

@Service
public class UserRoleServiceImpl {

    private final RoleRepositoryDomain roleRepository;

    private final UserRoleRepositoryDomain userRoleRepository;

    private final UtilUserServiceImpl utilUser;

    private final UserRoleMapper userRoleMapper;

    public UserRoleServiceImpl(
            RoleRepositoryDomain roleRepository,
            UserRoleRepositoryDomain userRoleRepository,
            UtilUserServiceImpl utilUser,
            UserRoleMapper userRoleMapper) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.utilUser = utilUser;
        this.userRoleMapper = userRoleMapper;
    }

    public List<UserRoleEntity> createUserRole(String userMail, String roleCode) {
        UserEntity user = utilUser.finUserMail(userMail);
        RoleEntity role =
                roleRepository.findByCode(roleCode).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        if (role.getDeleted()) {
            throw new AppException(ErrorCode.ROLE_DELETE);
        }
        boolean check = userRoleRepository.findByUserID(user.getUserID()).stream()
                .anyMatch(userRole -> userRole.getRoleID().equals(role.getId()));
        if (check) throw new AppException(ErrorCode.USER_HAD_ROLE);
        User userDomain = utilUser.getUserDomainById(user.getUserID());
        userDomain.addUserRole(UserRole.builder()
                .roleID(role.getId())
                .userID(userDomain.getUserID())
                .deleted(false)
                .build());
        return saveUserRoles(userRoleMapper.toUserRoles(userDomain.getUserRoles()));
    }

    public Boolean delete(String id) {
        UserRoleEntity userRole =
                userRoleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_ROLE_NOT_EXISTED));
        User userDomain = utilUser.getUserDomainById(userRole.getUserID());
        userDomain.deleteUserRoles(userRole.getId());
        userRole.setDeleted(true);
        userRoleRepository.save(userRole);
        return true;
    }

    // Check userRole not exits for add
    public List<UserRoleEntity> saveUserRoles(List<UserRoleEntity> userRoles) {
        List<UserRoleEntity> nonDuplicateRoles = userRoles.stream()
                .filter(userRole ->
                        userRoleRepository.existsByUserIdAndRoleId(userRole.getUserID(), userRole.getRoleID()))
                .collect(Collectors.toList());
        return userRoleRepository.saveAll(nonDuplicateRoles);
    }
}
