package com.evo.common.webapp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class ForbiddenTokenFilter extends OncePerRequestFilter {

    private final TokenCacheService tokenCacheService;

    public ForbiddenTokenFilter(TokenCacheService tokenCacheService) {
        this.tokenCacheService = tokenCacheService;

    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        log.info("Security context : {}", securityContext);
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) securityContext.getAuthentication();
        Jwt token = authentication.getToken();
        String idToken=token.getClaimAsString("jti");
//        if(tokenCacheService.isExistedToken(idToken)){
//            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
//        }
        // nếu có AuthenticationException thì sẽ chạy vào JwtAuthenticationEntryPoint
        if(tokenCacheService.isExistedToken(idToken)){
            throw new AuthenticationException("Invalid token"){};
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) {
            return true;
        }
        if (authentication instanceof JwtAuthenticationToken) {
            return !authentication.isAuthenticated();
        }
        return authentication instanceof AnonymousAuthenticationToken;
    }
}
