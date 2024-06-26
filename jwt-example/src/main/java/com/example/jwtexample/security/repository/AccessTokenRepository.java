package com.example.jwtexample.security.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class AccessTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${secret.access-token-validity-in-seconds}")
    private Long accessTokenValidityInSeconds;

    private String getKey(String token) {
        return "access_token:" + token;
    }

    public void mapAtToRt(String accessToken, String refreshToken) {
        redisTemplate.opsForValue().set(getKey(accessToken), refreshToken,
                accessTokenValidityInSeconds, TimeUnit.SECONDS);
    }

    public Optional<String> getRt(String accessToken) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(getKey(accessToken)));
    }

    public Boolean delete(String accessToken) {
        return redisTemplate.delete(getKey(accessToken));
    }

    public Boolean isExist(String accessToken) {
        return redisTemplate.hasKey(getKey(accessToken));
    }
}
