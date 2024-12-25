package com.pedalgenie.vote.domain.member.dto;

import com.pedalgenie.vote.domain.member.entity.Member;
import com.pedalgenie.vote.domain.member.entity.Part;
import com.pedalgenie.vote.domain.member.entity.Team;
import org.springframework.security.crypto.password.PasswordEncoder;

public record SignUpRequest(
        String loginId,
        String password,
        String email,
        String username,
        Part part,
        Team team
) {
    public Member toEntity(PasswordEncoder passwordEncoder){
        return Member.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .email(email)
                .username(username)
                .part(part)
                .team(team)
                .build();
    }
}
