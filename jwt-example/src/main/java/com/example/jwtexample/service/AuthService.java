package com.example.jwtexample.service;

import com.example.jwtexample.domain.Member;
import com.example.jwtexample.dto.RegisterRequestDto;
import com.example.jwtexample.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberFactory memberFactory;
    private final MemberRepository memberRepository;

    public void register(RegisterRequestDto registerRequestDto) {
        Member member = memberFactory.createUser(registerRequestDto);
        memberRepository.save(member);
    }
}
