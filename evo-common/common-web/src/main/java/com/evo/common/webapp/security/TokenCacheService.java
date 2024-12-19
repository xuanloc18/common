package com.evo.common.webapp.security;

public interface TokenCacheService {

    void invalidToken(String token);

    void invalidRefreshToken(String refreshToken);

    boolean isExistedToken(String token);

    boolean isExistedRefreshToken(String token);


}
