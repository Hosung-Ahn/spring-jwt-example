package com.example.jwtexample.dto;

import lombok.Data;

@Data
public class RefreshResponseDto {
    private String accessToken;
    private Long refreshTokenExpirationInMilliSeconds;

    public RefreshResponseDto(String accessToken, Long refreshTokenExpirationInMilliSeconds) {
        this.accessToken = accessToken;
        this.refreshTokenExpirationInMilliSeconds = refreshTokenExpirationInMilliSeconds;
    }
}
