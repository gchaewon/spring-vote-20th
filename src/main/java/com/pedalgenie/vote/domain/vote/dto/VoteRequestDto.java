package com.pedalgenie.vote.domain.vote.dto;

import com.pedalgenie.vote.domain.member.entity.Part;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteRequestDto {
    private String type;
    private Part part;
    private String voted;

    @Builder
    public VoteRequestDto(String type, String part, String voted) {
        this.type = type;
        this.voted = voted;

        if (part != null) {
            this.part = Part.valueOf(part);
        } else {
            this.part = null;
        }
    }
}

