package dev.cxl.iam_service.service;

import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.entity.Role;
import dev.cxl.iam_service.entity.User;
import dev.cxl.iam_service.entity.UserRole;
import dev.cxl.iam_service.respository.RoleRepository;
import dev.cxl.iam_service.respository.UserRoleRepository;

@Service
public class UserRoleService {

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    private final UtilUserService utilUser;

    public UserRoleService(
            RoleRepository roleRepository, UserRoleRepository userRoleRepository, UtilUserService utilUser) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.utilUser = utilUser;
    }

    public UserRole createUserRole(String userMail, String roleCode) {
        User user = utilUser.finUserMail(userMail);
        Role role = roleRepository.findByCode(roleCode).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        if (role.getDeleted()) {
            throw new AppException(ErrorCode.ROLE_DELETE);
        }
        Boolean check = userRoleRepository.findByUserID(user.getUserID()).stream()
                .anyMatch(userRole -> userRole.getRoleID().equals(role.getId()));

        if (check) throw new AppException(ErrorCode.USER_HAD_ROLE);

        return userRoleRepository.save(UserRole.builder()
                .userID(user.getUserID())
                .roleID(role.getId())
                .deleted(false)
                .build());
    }

    public Boolean delete(String id) {
        UserRole role =
                userRoleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_ROLE_NOT_EXISTED));
        role.setDeleted(true);
        userRoleRepository.save(role);
        return true;
    }
}
