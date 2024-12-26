package com.pedalgenie.vote.domain.member.controller;

import com.pedalgenie.vote.domain.member.dto.SignUpRequest;
import com.pedalgenie.vote.domain.member.dto.SignUpResponse;
import com.pedalgenie.vote.domain.member.service.AuthService;
import com.pedalgenie.vote.global.ResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService memberService;

    @Operation(summary="회원가입")
    @PostMapping
    public ResponseEntity<ResponseTemplate<SignUpResponse>> signUp(@RequestBody final SignUpRequest request){
        SignUpResponse signUpResponse = memberService.signUp(request);
        return ResponseTemplate.createTemplate(HttpStatus.CREATED,true,"회원가입 성공",signUpResponse);
    }


}
