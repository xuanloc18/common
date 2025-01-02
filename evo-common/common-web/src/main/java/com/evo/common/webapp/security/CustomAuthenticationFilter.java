package com.evo.common.webapp.security;

import com.evo.common.UserAuthentication;
import com.evo.common.UserAuthority;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private final AuthorityService authorityService;

    public CustomAuthenticationFilter(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("CustomAuthenticationFilter");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) securityContext.getAuthentication();
        Jwt token = authentication.getToken();
        String claim = "", id = "";
        Boolean isRoot ;
        Boolean isClient = Boolean.FALSE;

        Set<SimpleGrantedAuthority> grantedPermissions = new HashSet<>();
        if (StringUtils.hasText(token.getClaimAsString("client_id"))) {
            claim = "client_id";
            id = token.getClaim("client_id");
            isClient = Boolean.TRUE;
        } else if (StringUtils.hasText(token.getClaimAsString("given_name"))) {
            claim = "given_name";
            id = token.getClaim("sub");
        }

        Optional<UserAuthority> optionalUserAuthority = enrichAuthority(token, claim);
        UserAuthority userAuthority = optionalUserAuthority.orElse(null);

        if (userAuthority != null && userAuthority.getGrantedPermissions() != null) {
            grantedPermissions = userAuthority.getGrantedPermissions().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
        isRoot=userAuthority.getIsRoot();
        User principal = new User(id, "", grantedPermissions);
        AbstractAuthenticationToken auth = new UserAuthentication(principal, token, grantedPermissions, isRoot, isClient);

        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        return !(authentication instanceof JwtAuthenticationToken);
    }

    private Optional<UserAuthority> enrichAuthority(Jwt token, String claim) {
        String id = token.getClaimAsString("sub");
        if (id == null) {
            log.warn("Claim {} is missing or invalid", claim);
            return Optional.empty();
        }

        switch (claim) {
            case "client_id":
                return Optional.ofNullable(authorityService.getClientAuthority(id));
            case "given_name":
                return Optional.ofNullable(authorityService.getUserAuthority(UUID.fromString(id)));
            default:
                log.warn("Unknown claim type: {}", claim);
                return Optional.empty();
        }
    }

}
