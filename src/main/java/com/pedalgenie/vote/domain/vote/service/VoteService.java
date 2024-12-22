package com.pedalgenie.vote.domain.vote.service;

import com.pedalgenie.vote.domain.member.entity.Member;
import com.pedalgenie.vote.domain.member.entity.Part;
import com.pedalgenie.vote.domain.member.entity.Team;
import com.pedalgenie.vote.domain.vote.dto.VoteResultDto;
import com.pedalgenie.vote.domain.vote.repository.LeaderVoteRepository;
import com.pedalgenie.vote.domain.member.repostiory.MemberRepository;
import com.pedalgenie.vote.domain.vote.dto.VoteRequestDto;
import com.pedalgenie.vote.domain.vote.dto.VoteResponseDto;
import com.pedalgenie.vote.domain.vote.entity.LeaderVote;
import com.pedalgenie.vote.domain.vote.entity.TeamVote;
import com.pedalgenie.vote.domain.vote.repository.TeamVoteRepository;
import com.pedalgenie.vote.global.exception.CustomException;
import com.pedalgenie.vote.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {
    private final MemberRepository memberRepository;
    private final TeamVoteRepository teamVoteRepository;
    private final LeaderVoteRepository leaderVoteRepository;

    // 투표 생성 메서드
    @Transactional
    public VoteResponseDto createVote(VoteRequestDto requestDto, Long memberId) {
        Member voter = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_MEMBER_ID));

        if ("leader".equalsIgnoreCase(requestDto.getType())) {
            return createLeaderVote(voter, requestDto);
        } else if ("team".equalsIgnoreCase(requestDto.getType())) {
            return createTeamVote(voter, requestDto);
        } else {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }

    private VoteResponseDto createLeaderVote(Member voter, VoteRequestDto requestDto) {
        // 유저가 이미 투표한 경우 예외 처리
        boolean hasVoted = leaderVoteRepository.existsByMember(voter);
        if (hasVoted) {
            throw new CustomException(ErrorCode.ALREADY_VOTED);
        }

        Part part = requestDto.getPart(); // 투표받는 파트
        String partMember = requestDto.getVoted(); // 투표받는 후보자

        // 투표자가 속하지 않은 파트에 투표를 시도하는 경우 예외처리
        if (!voter.getPart().equals(part)) {
            throw new CustomException(ErrorCode.CANNOT_VOTE_FOR_ANOTHER_PART);
        }

        // 투표받는 후보자가 해당 파트에 속하지 않는 경우 예외처리
        if (!part.getMembers().contains(partMember)) {
            throw new CustomException(ErrorCode.NOT_EXISTS_PART_MEMBER);
        }

        LeaderVote leaderVote = LeaderVote.builder()
                .member(voter)
                .part(part)
                .partMember(partMember)
                .build();

        leaderVoteRepository.save(leaderVote);

        return VoteResponseDto.builder()
                .voter(voter.getName())
                .voted(partMember)
                .build();
    }

    private VoteResponseDto createTeamVote(Member voter, VoteRequestDto requestDto) {
        // 유저가 이미 투표한 경우 예외 처리
        boolean hasVoted = teamVoteRepository.existsByMember(voter);
        if (hasVoted) {
            throw new CustomException(ErrorCode.ALREADY_VOTED);
        }

        String votedTeam = requestDto.getVoted(); // 투표받는 팀
        Team voterTeam = voter.getTeam(); // 투표자가 속한 팀

        // 자신이 속한 팀에 투표할 경우 예외처리
        if (voterTeam.name().equals(votedTeam)) {
            throw new CustomException(ErrorCode.CANNOT_VOTE_FOR_OWN_TEAM);
        }

        TeamVote teamVote = TeamVote.builder()
                .member(voter)
                .team(Team.valueOf(votedTeam))
                .build();

        teamVoteRepository.save(teamVote);

        return VoteResponseDto.builder()
                .voter(voter.getName())
                .voted(votedTeam)
                .build();
    }

    // 후보 조회 메서드
    public List<String> getCandidates(String type, String part) {
        if (type.equalsIgnoreCase("leader")) {
            return getPartCandidates(part);
        } else if (type.equalsIgnoreCase("team")) {
            return getTeamCandidates();
        } else {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }

    // 파트별 후보 조회 메서드
    private List<String> getPartCandidates(String partName) {
        if (partName == null || partName.trim().isEmpty()) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        if (partName.equalsIgnoreCase("FRONT")) {
            return Part.FRONT.getMembers();
        } else if (partName.equalsIgnoreCase("BACK")) {
            return Part.BACK.getMembers();
        } else {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }

    // 팀별 후보 조회 메서드
    private List<String> getTeamCandidates(){
        return List.of(Team.PHOTO_GROUND.name(), Team.ANGEL_BRIDGE.name(), Team.PEDAL_GENIE.name(),
                Team.CAKE_WAY.name(), Team.COFFEE_DEAL.name());
    }

    // 투표 결과 조회 메서드
    public List<VoteResultDto> getResults(String type, String partName) {
        if (type.equalsIgnoreCase("leader")) {
            return getPartVoteResults(partName);
        } else if (type.equalsIgnoreCase("team")) {
            return getTeamVoteResults();
        } else {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }

    // 팀별 결과 조회 메서드
    public List<VoteResultDto> getTeamVoteResults() {
        // 모든 팀 조회
        List<Team> teams = Arrays.asList(Team.values());

        // 투표 결과 조회 및 map 생성
        List<Object[]> results = teamVoteRepository.countVotesByTeam();
        Map<Team, Long> teamVotesMap = results.stream()
                .collect(Collectors.toMap(result -> (Team) result[0], result -> ((Number) result[1]).longValue()));

        // 모든 팀에 대해 결과 생성, 없으면 0표로 처리
        return teams.stream()
                .map(team -> VoteResultDto.builder()
                        .name(team.name())
                        .votes(teamVotesMap.getOrDefault(team, 0L).intValue())
                        .build())
                .sorted((r1, r2) -> Integer.compare(r2.getVotes(), r1.getVotes())) // 내림차순 정렬
                .collect(Collectors.toList());
    }



    // 파트별 결과 조회 메서드
    public List<VoteResultDto> getPartVoteResults(String partName) {
        Part part = Part.valueOf(partName.toUpperCase());

        // 파트별 투표 결과 조회
        List<Object[]> results = leaderVoteRepository.countVotesByPart(part);

        // 투표 결과를 map으로 변환
        Map<String, Integer> voteCountMap = results.stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0], // 후보 이름
                        result -> ((Number) result[1]).intValue() // 투표 수
                ));

        // 모든 후보에 대해 결과 생성, 없으면 0표로 처리
        return part.getMembers().stream()
                .map(member -> VoteResultDto.builder()
                        .name(member)
                        .team(part.name())
                        .votes(voteCountMap.getOrDefault(member, 0))
                        .build())
                .sorted(Comparator.comparingInt(VoteResultDto::getVotes).reversed()
                        .thenComparing(VoteResultDto::getName)) // 투표 수 내림차순, 이름순 정렬
                .collect(Collectors.toList());
    }
}
