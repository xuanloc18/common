package com.evo.common.webapp.security;

import com.evo.common.UserAuthentication;
import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Slf4j
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        String requiredPermission = permission.toString()+"."+targetDomainObject.toString();
        log.info("Matching permissionwith pattern: {}", requiredPermission);

        if (!(authentication instanceof UserAuthentication userAuthentication)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if (userAuthentication.isRoot()) {
            return true;
        }
        if ((userAuthentication.isClient())){
            return true;
        }

        return userAuthentication.getGrantedPermissions().stream()
                .anyMatch(p -> p.equalsIgnoreCase(requiredPermission));

    }
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, null, permission);
    }
}
