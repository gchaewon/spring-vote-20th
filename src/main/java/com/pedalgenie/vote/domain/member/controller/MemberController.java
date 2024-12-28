package com.pedalgenie.vote.domain.member.controller;

import com.pedalgenie.vote.domain.auth.CustomUserDetails;
import com.pedalgenie.vote.domain.member.dto.SignUpResponse;
import com.pedalgenie.vote.domain.member.service.MemberService;
import com.pedalgenie.vote.global.ResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary="회원 정보 조회")
    @GetMapping
    public ResponseEntity<ResponseTemplate<SignUpResponse>> memberInfo(@AuthenticationPrincipal CustomUserDetails userDetails){

        Long memberId = userDetails.getMemberId();
        SignUpResponse signUpResponse = memberService.getMemberInfo(memberId);
        return ResponseTemplate.createTemplate(HttpStatus.OK,true,"회원 정보 조회 성공", signUpResponse);
    }


}
