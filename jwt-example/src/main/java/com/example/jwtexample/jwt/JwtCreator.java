package com.example.jwtexample.jwt;

import com.example.jwtexample.repository.AuthorityRepository;
import com.example.jwtexample.security.userDetails.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtCreator {
    @Value("${secret.secret-key}")
    private String secret;
    @Value("${secret.access-token-validity-in-seconds}")
    private Long accessTokenValidityInSeconds;
    @Value("${secret.refresh-token-validity-in-seconds}")
    private Long refreshTokenValidityInSeconds;
    private Key key;

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createAccessToken(Authentication authentication) {
        Date validity = getDateAfterSeconds(this.accessTokenValidityInSeconds);

        return createToken(authentication, validity);
    }

    public String createAccessToken(Long memberId, Collection<? extends GrantedAuthority> authorities) {
        Date validity = getDateAfterSeconds(this.accessTokenValidityInSeconds);
        return createToken(memberId, authorities, validity);
    }

    public String createRefreshToken(Authentication authentication) {
        Date validity = getDateAfterSeconds(this.refreshTokenValidityInSeconds);
        return createToken(authentication, validity);
    }

    public String createRefreshToken(Long memberId, Collection<? extends GrantedAuthority> authorities) {
        Date validity = getDateAfterSeconds(this.refreshTokenValidityInSeconds);
        return createToken(memberId, authorities, validity);
    }

    private Date getDateAfterSeconds(long seconds) {
        return new Date(System.currentTimeMillis() + seconds * 1000);
    }

    private String createToken(Authentication authentication, Date validity) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .claim("memberId", userDetails.getMemberId())
                .claim("authorities", authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    private String createToken(Long memberId, Collection<? extends GrantedAuthority> authorities, Date validity) {
        String authoritiesString = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .claim("memberId", memberId)
                .claim("authorities", authoritiesString)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }
}
