package com.example.jwtexample.security.service;

import com.example.jwtexample.jwt.JwtClaimReader;
import com.example.jwtexample.jwt.JwtValidator;
import com.example.jwtexample.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthenticationCreator {
    private final AuthorityRepository authorityRepository;
    private final JwtValidator jwtValidator;
    private final JwtClaimReader jwtClaimReader;


    public Authentication createAuthentication(String accessToken) {
        jwtValidator.validateAccessToken(accessToken);

        Long memberId = jwtClaimReader.getMemberId(accessToken);

        Collection<GrantedAuthority> authorities = authorityRepository.findAllByMemberId(memberId)
                .stream()
                .map(authority -> (GrantedAuthority) authority::getName)
                .toList();

        return new UsernamePasswordAuthenticationToken(memberId, accessToken, authorities);
    }
}
