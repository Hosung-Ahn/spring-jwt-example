package com.example.jwtexample.controller;

import com.example.jwtexample.dto.LoginRequestDto;
import com.example.jwtexample.dto.LoginResponseDto;
import com.example.jwtexample.dto.RefreshResponseDto;
import com.example.jwtexample.dto.RegisterRequestDto;
import com.example.jwtexample.security.service.AtRtDto;
import com.example.jwtexample.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto registerRequestDto) {
        authService.register(registerRequestDto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/admin-register")
    public ResponseEntity<String> adminRegister(@RequestBody RegisterRequestDto registerRequestDto) {
        authService.adminRegister(registerRequestDto);
        return ResponseEntity.ok("관리자 회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        AtRtDto login = authService.login(loginRequestDto);

        HttpCookie refreshTokenCookie = getRefreshTokenCookie(login.getRefreshToken(), login.getRefreshTokenExpirationFromNowInMS());

        return ResponseEntity.ok()
                .header("Set-Cookie", refreshTokenCookie.toString())
                .body(new LoginResponseDto(login.getMemberId(), login.getAccessToken()));
    }

    private HttpCookie getRefreshTokenCookie(String refreshToken, long refreshTokenExpirationFromNowInSeconds) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .maxAge(refreshTokenExpirationFromNowInSeconds)
                // secure 는 https 에서만 사용 가능합니다.
//                .secure(true)
                .httpOnly(true)
                .build();
    }

    @GetMapping("/refresh")
    public ResponseEntity<RefreshResponseDto> refresh(@CookieValue String refreshToken) {
        AtRtDto refresh = authService.refresh(refreshToken);

        Long refreshTokenExpirationFromNowInMS = refresh.getRefreshTokenExpirationFromNowInMS();
        HttpCookie refreshTokenCookie = getRefreshTokenCookie(refresh.getRefreshToken(), refreshTokenExpirationFromNowInMS);

        return ResponseEntity.ok()
                .header("Set-Cookie", refreshTokenCookie.toString())
                .body(new RefreshResponseDto(refresh.getAccessToken(), refreshTokenExpirationFromNowInMS));
    }
}
