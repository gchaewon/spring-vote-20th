package com.pedalgenie.vote.domain.vote.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoteResultDto {
    private String name;
    private String team;
    private int votes;
}
