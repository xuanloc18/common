package com.evo.common.webapp.security.impl;

import com.evo.common.webapp.security.TokenCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenCacheServiceImpl implements TokenCacheService {

    @Override
    public void invalidToken(String token) {

    }

    @Override
    public void invalidRefreshToken(String refreshToken) {

    }

    @Override
    public boolean isExisted(String cacheName, String token) {
        return true;
    }
}
