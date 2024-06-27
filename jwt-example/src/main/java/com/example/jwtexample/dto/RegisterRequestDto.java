package com.example.jwtexample.dto;

import lombok.Data;

@Data
public class RegisterRequestDto {
    String email;
    String password;

    public RegisterRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
