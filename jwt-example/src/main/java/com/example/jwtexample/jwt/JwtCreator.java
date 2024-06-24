package com.example.jwtexample.jwt;

import com.example.jwtexample.domain.Authority;
import com.example.jwtexample.repository.AuthorityRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
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

    private final AuthorityRepository authorityRepository;

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createAccessToken(Long memberId) {
        Date validity = getDateAfterSeconds(this.accessTokenValidityInSeconds);

        return createToken(memberId, validity);
    }

    public String createRefreshToken(Long memberId) {
        Date validity = getDateAfterSeconds(this.refreshTokenValidityInSeconds);
        return createToken(memberId, validity);
    }

    private Date getDateAfterSeconds(long seconds) {
        return new Date(System.currentTimeMillis() + seconds * 1000);
    }

    private String createToken(Long memberId, Date validity) {
        List<Authority> authorities = authorityRepository.findAllByMemberId(memberId);
        return Jwts.builder()
                .claim("memberId", memberId)
                .claim("authorities", authorities.stream()
                        .map(Authority::getName)
                        .collect(Collectors.joining(",")))
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }
}
