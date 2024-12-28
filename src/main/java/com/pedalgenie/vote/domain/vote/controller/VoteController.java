package com.pedalgenie.vote.domain.vote.controller;

import com.pedalgenie.vote.domain.auth.CustomUserDetails;
import com.pedalgenie.vote.domain.vote.dto.VoteResultDto;
import com.pedalgenie.vote.domain.vote.service.VoteService;
import com.pedalgenie.vote.domain.vote.dto.VoteRequestDto;
import com.pedalgenie.vote.domain.vote.dto.VoteResponseDto;
import com.pedalgenie.vote.global.ResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Vote Controller", description="투표 API \n 생성, 조회 로직을 포함합니다.")
public class VoteController {
    private final VoteService voteService;

    // 투표 생성
    @Operation(summary = "투표 생성")
    @PostMapping("/votes")
    public ResponseEntity<ResponseTemplate<VoteResponseDto>> createVote(
            @RequestParam String type,
            @RequestParam(required = false) String part,
            @RequestParam String voted,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId(); // 인증 정보로 변경

        VoteRequestDto requestDto = VoteRequestDto.builder()
                .type(type)
                .part(part)
                .voted(voted)
                .build();
        VoteResponseDto voteDto = voteService.createVote(requestDto, memberId);

        return ResponseTemplate.createTemplate(HttpStatus.CREATED, true, "투표 생성 성공", voteDto);
    }

    // 후보 조회
    @Operation(summary = "후보 조회")
    @GetMapping("/api/votes/candidates")
    public ResponseEntity<ResponseTemplate<List<String>>> getCandidates(
            @RequestParam String type,
            @RequestParam(required = false) String part
    ){

        List<String> candidateList = voteService.getCandidates(type, part);
        return ResponseTemplate.createTemplate(HttpStatus.OK, true, "후보 조회 성공", candidateList);
    }

    // 투표 결과 조회
    @Operation(summary = "투표 결과 조회")
    @GetMapping("/api/votes/results")
    public ResponseEntity<ResponseTemplate<List<VoteResultDto>>> getResults(
            @RequestParam String type,
            @RequestParam(required = false) String part
    ){
        List<VoteResultDto> resultList = voteService.getResults(type, part);
        return ResponseTemplate.createTemplate(HttpStatus.OK, true, "투표 결과 조회 성공", resultList);
    }
}
