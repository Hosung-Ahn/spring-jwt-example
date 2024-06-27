package com.example.jwtexample.jwt;

import com.example.jwtexample.jwt.exception.InvalidJwtException;
import com.example.jwtexample.jwt.exception.InvalidRefreshException;
import com.example.jwtexample.security.repository.AccessTokenRepository;
import com.example.jwtexample.security.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtValidator {
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtClaimReader jwtClaimReader;

    private void validateToken(String jwtToken) {
        try {
            jwtClaimReader.getClaims(jwtToken);
        } catch (SignatureException e) {
            log.error("Invalid JWT signature.");
            throw new InvalidJwtException("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token.");
            throw new InvalidJwtException("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.");
            throw new InvalidJwtException("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
            throw new InvalidJwtException("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
            throw new InvalidJwtException("JWT claims string is empty.");
        }
    }

    public void validateRefreshToken(String refreshToken) {
        try {
            validateToken(refreshToken);
        } catch (InvalidJwtException e) {
            throw new InvalidRefreshException("refresh token is invalid.");
        }
        if (!refreshTokenRepository.isExist(refreshToken)) {
            throw new InvalidRefreshException("refresh token expired or deleted.");
        }
    }

    public void validateAccessToken(String accessToken) {
        validateToken(accessToken);
        if (!accessTokenRepository.isExist(accessToken)) {
            throw new InvalidJwtException("access token expired or deleted.");
        }
    }
}
