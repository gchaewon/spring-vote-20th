package com.pedalgenie.vote.domain.auth;

import com.pedalgenie.vote.domain.member.entity.Member;
import com.pedalgenie.vote.domain.member.repostiory.MemberRepository;
import com.pedalgenie.vote.global.exception.CustomException;
import com.pedalgenie.vote.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public UserDetails loadUserByUsername(final String loginId) throws UsernameNotFoundException{
        final Member member= memberRepository.findByLoginId(loginId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXISTS_MEMBER_ID));

        return new CustomUserDetails(member);
    }

}
