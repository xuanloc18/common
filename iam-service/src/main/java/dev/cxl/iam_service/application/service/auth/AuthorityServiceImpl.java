package dev.cxl.iam_service.application.service.auth;

import java.util.List;
import java.util.UUID;

import dev.cxl.iam_service.domain.repository.PermissionRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.evo.common.UserAuthority;
import com.evo.common.webapp.security.AuthorityService;

import dev.cxl.iam_service.infrastructure.entity.Permission;
import dev.cxl.iam_service.infrastructure.entity.User;
import dev.cxl.iam_service.infrastructure.persistent.JpaPermissionRespository;
import dev.cxl.iam_service.application.service.UtilUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    private final PermissionRepository permissionRepository;
    private final UtilUserService utilUserService;

    @Override
    public UserAuthority getUserAuthority(UUID userId) {
        User user = utilUserService.finUserId(userId.toString());
        List<Permission> permissions = permissionRepository.findPermissionIdByUser(userId.toString());
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
