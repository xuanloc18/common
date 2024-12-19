package com.evo.common.webapp.security.impl;

import com.evo.common.webapp.security.TokenCacheService;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j

public class TokenCacheServiceImpl implements TokenCacheService {
    @NonFinal
    @Value("${jwt.valid-duration}")
    protected Long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected Long REFRESHABLE_DURATION;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Override
    public void invalidToken(String token) {
        // Thêm token vào Redis List
        redisTemplate.opsForList().leftPush("BLACKLIST_TOkEN", token);
        // Đặt thời gian hết hạn cho toàn bộ khóa "BLACKLIST"
        redisTemplate.expire("BLACKLIST_TOkEN", VALID_DURATION, TimeUnit.SECONDS);

    }

    @Override
    public void invalidRefreshToken(String refreshToken) {
        // Thêm token vào Redis List
        redisTemplate.opsForList().leftPush("BLACKLIST_REFRESH_TOkEN", refreshToken);
        // Đặt thời gian hết hạn cho toàn bộ khóa "BLACKLIST"
        redisTemplate.expire("BLACKLIST_REFRESH_TOkEN", REFRESHABLE_DURATION, TimeUnit.SECONDS);

    }

    @Override
    public boolean isExistedToken(String token) {
        List<String> list = redisTemplate.opsForList().range("BLACKLIST_TOkEN", 0, -1);
        return list != null && list.contains(token);
    }

    @Override
    public boolean isExistedRefreshToken(String refreshToken) {
        List<String> list = redisTemplate.opsForList().range("BLACKLIST_REFRESH_TOkEN", 0, -1);
        return list != null && list.contains(refreshToken);
    }



}
