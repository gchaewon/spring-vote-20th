package com.pedalgenie.vote.domain.member.entity;

import com.pedalgenie.vote.global.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long memberId;

    @NotNull
    private String loginId;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private Part part;

    @Enumerated(EnumType.STRING)
    private Team team;

    @Builder
    public Member(String loginId, String name, String email, String password, Part part, Team team) {
        this.loginId = loginId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.part = part;
        this.team = team;
    }
}
