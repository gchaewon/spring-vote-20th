package com.pedalgenie.vote.domain.vote.entity;

import com.pedalgenie.vote.domain.member.entity.Member;
import com.pedalgenie.vote.domain.member.entity.Team;
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
public class TeamVote extends Vote{
    @Enumerated(EnumType.STRING)
    @NotNull
    private Team team;

    @Builder
    public TeamVote(Member member, Team team){
        super(member);
        this.team = team;
    }
}
