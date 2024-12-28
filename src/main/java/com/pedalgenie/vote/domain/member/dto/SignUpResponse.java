package com.pedalgenie.vote.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pedalgenie.vote.domain.member.entity.Member;
import com.pedalgenie.vote.domain.member.entity.Part;
import com.pedalgenie.vote.domain.member.entity.Team;

@JsonInclude(JsonInclude.Include.NON_NULL) // null인 필드 json에서 제외
public record SignUpResponse(
        String loginId,
        String username,
        Part part,
        Team team
) {
    public static SignUpResponse from(Member member){
        return new SignUpResponse(member.getLoginId(),member.getUsername(), member.getPart(), member.getTeam());
    }
}
