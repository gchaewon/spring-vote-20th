package com.pedalgenie.vote.domain.member.service;

import com.pedalgenie.vote.domain.member.dto.SignUpResponse;
import com.pedalgenie.vote.domain.member.entity.Member;
import com.pedalgenie.vote.domain.member.repostiory.MemberRepository;
import com.pedalgenie.vote.global.exception.CustomException;
import com.pedalgenie.vote.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 회원 조회 메서드
    public SignUpResponse getMemberInfo(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_EXISTS_MEMBER_ID));
        return SignUpResponse.from(member);
    }

}
