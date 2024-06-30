package com.example.jwtexample.security.service;

import com.example.jwtexample.jwt.JwtClaimReader;
import com.example.jwtexample.jwt.JwtCreator;
import com.example.jwtexample.security.repository.AccessTokenRepository;
import com.example.jwtexample.security.repository.RefreshTokenRepository;
import com.example.jwtexample.security.userDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AtRtService {
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtCreator jwtCreator;
    private final JwtClaimReader jwtClaimReader;

    public AtRtDto create(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long memberId = userDetails.getMemberId();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        return getAtRtDto(memberId, authorities);
    }

    public AtRtDto refresh(String refreshToken) {
        // refreshToken 에서 memberId 를 찾습니다. 이때 유효성 검사도 자동으로 진행됩니다.
        Long memberId = jwtClaimReader.getMemberId(refreshToken);
        Collection<? extends GrantedAuthority> authorities = jwtClaimReader.getAuthorities(refreshToken);

        // refreshToken 에 매핑된 accessToken 가 존재한다면 찾아 삭제합니다.
        refreshTokenRepository.getAt(refreshToken).ifPresent(
                accessTokenRepository::delete
        );
        // refreshToken 을 삭제합니다.
        refreshTokenRepository.delete(refreshToken);

        return getAtRtDto(memberId, authorities);
    }

    private AtRtDto getAtRtDto(Long memberId, Collection<? extends GrantedAuthority> authorities) {
        String newAccessToken = jwtCreator.createAccessToken(memberId, authorities);
        String newRefreshToken = jwtCreator.createRefreshToken(memberId, authorities);

        accessTokenRepository.mapAtToRt(newAccessToken, newRefreshToken);
        refreshTokenRepository.mapRtToAt(newRefreshToken, newAccessToken);

        Long expirationInMS = jwtClaimReader.getExpirationInMilliseconds(newRefreshToken);

        return new AtRtDto(memberId, newAccessToken, newRefreshToken, expirationInMS);
    }

    /**
     * accessToken, refreshToken 을 모두 삭제합니다. <br>
     * accessToken 만 있을 경우 refreshToken 을 찾아 삭제합니다. <br>
     * refreshToken 만 있을 경우 accessToken 을 찾아 삭제합니다.
     * @param accessToken
     * @param refreshToken
     */
    public void deleteAll(String accessToken, String refreshToken) {
        if (accessToken != null && refreshToken != null) {
            accessTokenRepository.delete(accessToken);
            refreshTokenRepository.delete(refreshToken);
        } else if (accessToken != null) {
            accessTokenRepository.getRt(accessToken).ifPresent(
                    refreshTokenRepository::delete
            );
            accessTokenRepository.delete(accessToken);
        } else if (refreshToken != null) {
            refreshTokenRepository.getAt(refreshToken).ifPresent(
                    accessTokenRepository::delete
            );
            refreshTokenRepository.delete(refreshToken);
        }
    }
}
