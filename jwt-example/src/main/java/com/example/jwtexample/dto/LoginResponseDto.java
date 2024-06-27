package com.example.jwtexample.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private Long memberId;
    private String accessToken;

    public LoginResponseDto(Long memberId, String accessToken) {
        this.memberId = memberId;
        this.accessToken = accessToken;
    }
}
