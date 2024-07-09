package com.example.jwtexample.service;

import com.example.jwtexample.domain.Member;
import com.example.jwtexample.dto.LoginRequestDto;
import com.example.jwtexample.dto.LoginResponseDto;
import com.example.jwtexample.dto.RegisterRequestDto;
import com.example.jwtexample.repository.MemberRepository;
import com.example.jwtexample.security.service.AtRtDto;
import com.example.jwtexample.security.service.AtRtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberFactory memberFactory;
    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private final AtRtService atRtService;

    public void register(RegisterRequestDto registerRequestDto) {
        Member member = memberFactory.createUser(registerRequestDto);
        memberRepository.save(member);
    }



    public AtRtDto login(LoginRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        // 여기서 생성되는 authentication 은 CustomUserDetailsService 에서 생성한 CustomUserDetails 를 가지고 있습니다.
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // SecurityContextHolder 에 인증 정보를 저장합니다.
        // 이후 현재 스레드에서 SecurityContextHolder.getContext().getAuthentication() 으로 인증 정보를 가져올 수 있습니다.
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return atRtService.create(authenticate);
    }

    public AtRtDto refresh(String refreshToken) {
        return atRtService.refresh(refreshToken);
    }

    public void adminRegister(RegisterRequestDto registerRequestDto) {
        Member member = memberFactory.createAdmin(registerRequestDto);
        memberRepository.save(member);
    }
}
