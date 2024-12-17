package com.evo.common.webapp.security;

import com.evo.common.UserAuthority;

import java.util.UUID;

public interface AuthorityService {
    UserAuthority getUserAuthority(UUID userId);

    UserAuthority getUserAuthority(String username);

    UserAuthority getClientAuthority(UUID clientId);

}
