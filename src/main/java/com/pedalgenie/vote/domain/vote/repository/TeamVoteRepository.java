package com.pedalgenie.vote.domain.vote.repository;

import com.pedalgenie.vote.domain.member.entity.Member;
import com.pedalgenie.vote.domain.vote.entity.TeamVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamVoteRepository extends JpaRepository<TeamVote, Long> {
    boolean existsByMember(Member member);
    @Query("SELECT t.team, COUNT(t) FROM TeamVote t GROUP BY t.team")
    List<Object[]> countVotesByTeam();
}
