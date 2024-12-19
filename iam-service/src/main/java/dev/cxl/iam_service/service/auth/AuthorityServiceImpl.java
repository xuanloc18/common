package dev.cxl.iam_service.service.auth;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.evo.common.UserAuthority;
import com.evo.common.webapp.security.AuthorityService;

import dev.cxl.iam_service.entity.Permission;
import dev.cxl.iam_service.entity.User;
import dev.cxl.iam_service.respository.PermissionRespository;
import dev.cxl.iam_service.service.UtilUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    private final PermissionRespository permissionRespository;
    private final UtilUserService utilUserService;

    @Override
    public UserAuthority getUserAuthority(UUID userId) {
        User user = utilUserService.finUserId(userId.toString());
        List<Permission> permissions = permissionRespository.findPermissionIdByUser(userId.toString());
        log.info("---USER GRANT---" + mapRolesToAuthorities(permissions).toString());
        return UserAuthority.builder()
                .userId(user.getUserID())
                .isRoot(user.isRoot())
                .grantedPermissions(mapRolesToAuthorities(permissions))
                .build();
    }

    @Override
    public UserAuthority getClientAuthority(String clientId) {
        return UserAuthority.builder().userId(clientId.toString()).isRoot(true).build();
    }

    private List<String> mapRolesToAuthorities(List<Permission> permissions) {

        return permissions.stream()
                .map(permission -> (permission.getScope() + "." + permission.getResourceCode()))
                .toList();
    }
}
