package com.pedalgenie.vote.domain.vote.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VoteResponseDto {
    private String voter; // 투표한 사람 이름
    private String voted; // 투표 받은 사람 이름
}
