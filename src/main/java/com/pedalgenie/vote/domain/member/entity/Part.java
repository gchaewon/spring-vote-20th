package com.pedalgenie.vote.domain.member.entity;

import java.util.List;

public enum Part {
    FRONT(List.of("강다혜", "권혜인", "김류원", "박지수", "송유선", "윤영준", "이가빈", "이희원", "지민재", "최지원")),
    BACK(List.of("김연수", "남승현", "나혜인", "문서영", "임가현", "이한슬", "이채원", "유지민", "황서아", "최서지"));

    private final List<String> members;

    Part(List<String> members) {
        this.members = members;
    }
    public List<String> getMembers() {
        return members;
    }
}

