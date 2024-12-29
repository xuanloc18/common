package dev.cxl.iam_service.application.service.auth;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.evo.common.UserAuthority;
import com.evo.common.webapp.security.AuthorityService;

import dev.cxl.iam_service.application.service.impl.UtilUserServiceImpl;
import dev.cxl.iam_service.domain.repository.PermissionRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.PermissionEntity;
import dev.cxl.iam_service.infrastructure.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    private final PermissionRepositoryDomain permissionRepository;
    private final UtilUserServiceImpl utilUserService;

    @Override
    public UserAuthority getUserAuthority(UUID userId) {
        UserEntity user = utilUserService.finUserId(userId.toString());
        List<PermissionEntity> permissions = permissionRepository.findPermissionIdByUser(userId.toString());
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

    private List<String> mapRolesToAuthorities(List<PermissionEntity> permissions) {

        return permissions.stream()
                .map(permission -> (permission.getScope() + "." + permission.getResourceCode()))
                .toList();
    }
}
