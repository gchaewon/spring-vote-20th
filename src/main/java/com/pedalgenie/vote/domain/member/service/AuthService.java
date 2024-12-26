package com.pedalgenie.vote.domain.member.service;

import com.pedalgenie.vote.domain.auth.CustomUserDetailsService;
import com.pedalgenie.vote.domain.jwt.JwtUtil;
import com.pedalgenie.vote.domain.member.dto.SignUpRequest;
import com.pedalgenie.vote.domain.member.dto.SignUpResponse;
import com.pedalgenie.vote.domain.member.entity.Member;
import com.pedalgenie.vote.domain.member.repostiory.MemberRepository;
import com.pedalgenie.vote.global.exception.CustomException;
import com.pedalgenie.vote.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;


    // 회원 가입
    public SignUpResponse signUp(final SignUpRequest request){

        // 로그인 아이디 중복 검사
        if(memberRepository.existsByLoginId(request.loginId())){
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_LOGIN_ID);
        }
        // 이메일 중복 검사
        if(memberRepository.existsByEmail(request.email())){
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_MEMBER_EMAIL);
        }
        final Member member = request.toEntity(passwordEncoder);
        memberRepository.save(member);

        return SignUpResponse.from(member);

    }

}
