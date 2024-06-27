package com.example.jwtexample.security.service;

import lombok.Data;

@Data
public class AtRtDto {
    private Long memberId;
    private String accessToken;
    private String refreshToken;
    private Long refreshTokenExpirationFromNowInMS;

    public AtRtDto(Long memberId, String accessToken, String refreshToken, Long refreshTokenExpirationFromNowInMS) {
        this.memberId = memberId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.refreshTokenExpirationFromNowInMS = refreshTokenExpirationFromNowInMS;
    }
}
