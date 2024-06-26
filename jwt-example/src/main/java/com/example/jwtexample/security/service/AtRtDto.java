package com.example.jwtexample.security.service;

import lombok.Data;

@Data
public class AtRtDto {
    private String accessToken;
    private String refreshToken;

    public AtRtDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
