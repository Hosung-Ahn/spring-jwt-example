package com.example.jwtexample.dto;

import lombok.Data;

@Data
public class RegisterRequestDto {
    String email;
    String password;
}
