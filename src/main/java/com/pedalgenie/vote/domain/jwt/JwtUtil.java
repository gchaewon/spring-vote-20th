package com.pedalgenie.vote.domain.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;


@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret}")final String secret) {
//        final String algorithm = HS256.getValue();
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),"HmacSHA256");
    }

    public String createJwt(final String category,final String loginId, Long expiredMs){
        return Jwts.builder()
                .claim("category", category)
                .claim("loginId", loginId)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis()+ expiredMs)) // 만료 시간
                .signWith(secretKey, HS256)
                .compact();
    }

    // 토큰에서 로그인 아이디 추출 메서드
    public String getLoginId(final String token){
        Claims claims= getPayload(token);
        return claims.get("loginId", String.class);
    }

    // 토큰에서 카테고리(액세스, 리프레시) 추출 메서드
    public String getCategory(final String token){
        Claims claims= getPayload(token);
        return claims.get("category", String.class);
    }

    // 토큰 만료 검사 메서드
    public boolean isExpired(final String token){
        return getPayload(token)
                .getExpiration()
                .before(new Date());
    }

    // 토큰 페이로드 메서드
    private Claims getPayload(final String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
