package dev.cxl.iam_service.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;


import dev.cxl.iam_service.application.mapper.UserRoleMapper;
import dev.cxl.iam_service.domain.domainentity.UserDomain;
import dev.cxl.iam_service.domain.domainentity.UserRoleDomain;
import dev.cxl.iam_service.domain.repository.RoleRepository;
import dev.cxl.iam_service.domain.repository.UserRoleRepository;
import dev.cxl.iam_service.infrastructure.entity.Role;
import dev.cxl.iam_service.infrastructure.entity.User;
import dev.cxl.iam_service.infrastructure.entity.UserRole;

@Service
public class UserRoleService {

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    private final UtilUserService utilUser;

    private final UserRoleMapper userRoleMapper;

    public UserRoleService(
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository,
            UtilUserService utilUser,
            UserRoleMapper userRoleMapper) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.utilUser = utilUser;
        this.userRoleMapper = userRoleMapper;
    }

    public List<UserRole> createUserRole(String userMail, String roleCode) {
        User user = utilUser.finUserMail(userMail);
        Role role = roleRepository.findByCode(roleCode).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        if (role.getDeleted()) {
            throw new AppException(ErrorCode.ROLE_DELETE);
        }
        boolean check = userRoleRepository.findByUserID(user.getUserID()).stream()
                .anyMatch(userRole -> userRole.getRoleID().equals(role.getId()));
        if (check) throw new AppException(ErrorCode.USER_HAD_ROLE);
        UserDomain userDomain = utilUser.getUserDomain(user.getUserID());
        userDomain.addUserRole(UserRoleDomain.builder()
                .roleID(role.getId())
                .userID(userDomain.getUserID())
                .build());
       return saveUserRoles(userRoleMapper.toUserRoles(userDomain.getUserRoles()));
    }

    public void saveAllUserRoles(List<UserRoleDomain> userRoleDomains) {
        userRoleRepository.saveAll(userRoleMapper.toUserRoles(userRoleDomains));
    }

    public Boolean delete(String id) {
        UserRole userRole =
                userRoleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_ROLE_NOT_EXISTED));
        UserDomain userDomain = utilUser.getUserDomain(userRole.getUserID());
        userDomain.deleteUserRoles(userRole.getId());
        userRole.setDeleted(true);
        userRoleRepository.save(userRole);
        return true;
    }

    //Check userRole not exits for add
    public List<UserRole> saveUserRoles(List<UserRole> userRoles) {
        List<UserRole> nonDuplicateRoles = userRoles.stream()
                .filter(userRole -> !userRoleRepository.existsByUserIdAndRoleId(userRole.getUserID(), userRole.getRoleID()))
                .collect(Collectors.toList());
        return  userRoleRepository.saveAll(nonDuplicateRoles);
    }

}
