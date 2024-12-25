package com.pedalgenie.vote.domain.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenValidator {
    private final JwtUtil jwtUtil;

    // 토큰 만료 여부 확인
    public void validateTokenExpired(final String token){
        try{
            jwtUtil.isExpired(token);
        }catch (ExpiredJwtException e){
            throw new IllegalArgumentException(jwtUtil.getCategory(token)+" 토큰 만료");
        }
    }
}
