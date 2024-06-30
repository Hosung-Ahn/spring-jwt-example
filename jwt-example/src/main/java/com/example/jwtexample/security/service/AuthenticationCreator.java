package com.example.jwtexample.security.service;

import com.example.jwtexample.jwt.JwtClaimReader;
import com.example.jwtexample.jwt.JwtValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthenticationCreator {
    private final JwtValidator jwtValidator;
    private final JwtClaimReader jwtClaimReader;


    public Authentication createByAccessToken(String accessToken) {
        jwtValidator.validateAccessToken(accessToken);

        Long memberId = jwtClaimReader.getMemberId(accessToken);
        Collection<? extends GrantedAuthority> authorities = jwtClaimReader.getAuthorities(accessToken);

        return new UsernamePasswordAuthenticationToken(memberId, accessToken, authorities);
    }

    public Authentication createByRefreshToken(String refreshToken) {
        jwtValidator.validateRefreshToken(refreshToken);

        Long memberId = jwtClaimReader.getMemberId(refreshToken);
        Collection<? extends GrantedAuthority> authorities = jwtClaimReader.getAuthorities(refreshToken);

        return new UsernamePasswordAuthenticationToken(memberId, refreshToken, authorities);
    }
}
