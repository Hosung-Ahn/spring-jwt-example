package com.example.jwtexample.service;

import com.example.jwtexample.domain.EAuthority;
import com.example.jwtexample.domain.Member;
import com.example.jwtexample.dto.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class MemberFactory {
    private final PasswordEncoder passwordEncoder;

    public Member createUser(RegisterRequestDto registerRequestDto) {
        String email = registerRequestDto.getEmail(); // 이메일 중복 체크 등 추가적인 검증이 필요함
        String password = passwordEncoder.encode(registerRequestDto.getPassword());
        List<EAuthority> eAuthorities = List.of(EAuthority.ROLE_USER);
        return new Member(email, password, eAuthorities);
    }

    public Member createAdmin(RegisterRequestDto registerRequestDto) {
        String email = registerRequestDto.getEmail(); // 이메일 중복 체크 등 추가적인 검증이 필요함
        String password = passwordEncoder.encode(registerRequestDto.getPassword());
        List<EAuthority> eAuthorities = List.of(EAuthority.ROLE_USER, EAuthority.ROLE_ADMIN);
        return new Member(email, password, eAuthorities);
    }
}
