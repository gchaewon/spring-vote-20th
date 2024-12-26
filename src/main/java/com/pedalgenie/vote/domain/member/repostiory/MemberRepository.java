package com.pedalgenie.vote.domain.member.repostiory;

import com.pedalgenie.vote.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByLoginId(final String loginId);
    boolean existsByEmail(final String email);

    Optional<Member> findByUsername(String username);
}
