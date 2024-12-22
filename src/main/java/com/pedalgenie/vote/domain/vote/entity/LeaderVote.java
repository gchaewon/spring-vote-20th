package com.pedalgenie.vote.domain.vote.entity;

import com.pedalgenie.vote.domain.member.entity.Member;
import com.pedalgenie.vote.domain.member.entity.Part;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeaderVote extends Vote{
    @Enumerated(EnumType.STRING)
    @NotNull
    private Part part;

    @NotNull
    private String partMember;

    @Builder
    public LeaderVote(Member member, Part part, String partMember){
        super(member);
        this.part = part;
        this.partMember = partMember;
    }
}
