package com.example.jwtexample.security.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${secret.refresh-token-validity-in-seconds}")
    private Long refreshTokenValidityInSeconds;

    private String getKey(String token) {
        return "refresh_token:" + token;
    }

    public void mapAtToRt(String refreshToken, String accessToken) {
        redisTemplate.opsForValue().set(getKey(refreshToken), accessToken,
                refreshTokenValidityInSeconds, TimeUnit.SECONDS);
    }

    public Optional<String> getAt(String refreshToken) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(getKey(refreshToken)));
    }

    public Boolean delete(String refreshToken) {
        return redisTemplate.delete(getKey(refreshToken));
    }

    public Boolean isExist(String refreshToken) {
        return redisTemplate.hasKey(getKey(refreshToken));
    }
}
