package com.pedalgenie.vote.domain.vote.repository;

import com.pedalgenie.vote.domain.member.entity.Member;
import com.pedalgenie.vote.domain.member.entity.Part;
import com.pedalgenie.vote.domain.vote.entity.LeaderVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeaderVoteRepository extends JpaRepository<LeaderVote, Long> {
    boolean existsByMember(Member member);

    @Query("SELECT lv.partMember, COUNT(lv) " +
            "FROM LeaderVote lv " +
            "WHERE lv.part = :part " +
            "GROUP BY lv.partMember")
    List<Object[]> countVotesByPart(@Param("part") Part part);
}
