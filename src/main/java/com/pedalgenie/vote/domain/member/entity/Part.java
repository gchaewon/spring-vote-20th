package com.pedalgenie.vote.domain.member.entity;

import java.util.List;

public enum Part {
    FRONT(List.of("윤영준", "박지수", "송유선", "최지원", "김류원", "지민재", "강다혜", "이희원", "권혜인", "이가빈")),
    BACK(List.of("김연수", "남승현", "나혜인", "문서영", "임가현", "이한슬", "이채원", "유지민", "황서아", "최서지"));

    private final List<String> members;

    Part(List<String> members) {
        this.members = members;
    }
    public List<String> getMembers() {
        return members;
    }
}

