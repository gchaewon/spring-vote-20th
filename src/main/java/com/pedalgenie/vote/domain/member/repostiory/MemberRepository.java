package com.pedalgenie.vote.domain.member.repostiory;

import com.pedalgenie.vote.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
