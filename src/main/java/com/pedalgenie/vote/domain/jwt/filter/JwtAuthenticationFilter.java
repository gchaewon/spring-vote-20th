package com.pedalgenie.vote.domain.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedalgenie.vote.domain.jwt.JwtUtil;
import com.pedalgenie.vote.domain.member.dto.LoginRequest;
import com.pedalgenie.vote.domain.member.entity.Member;
import com.pedalgenie.vote.domain.member.repostiory.MemberRepository;
import com.pedalgenie.vote.global.exception.CustomException;
import com.pedalgenie.vote.global.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        setAuthenticationManager(authenticationManager); // 명시적으로 AuthenticationManager 설정
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {

        LoginRequest loginRequest;

        try {
            ObjectMapper objectMapper=new ObjectMapper();

            // JSON 요청 본문을 읽어 LoginRequestDto 객체로 변환
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginRequest = objectMapper.readValue(messageBody, LoginRequest.class);


            // 스프링 시큐리티에서 사용자의 인증 정보(username, password)를 검증하기 위해서는 token(dto)에 담아야 함
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());

            // token에 담은 값들의 검증을 위해 AuthenticationManager로 전달 -> 검증 진행
            return authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        } catch (IOException e) {
            throw new RuntimeException("요청 바디 파싱 에러");
        }
    }

    // 로그인 성공 시 실행하는 메서드 (JWT 토큰 발급)
    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                                final FilterChain filterChain, final Authentication authResult)
            throws IOException, ServletException{

        // CustomUserDetails의 메서드에서 추출한 값
        final String username = authResult.getName();

        // 권한 목록에 있는 권한을 추출(ROLE_USER)
        String role = authResult.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        // 토큰 생성
        String access = jwtUtil.createJwt("access", username,1000L * 60 * 60 * 2); // 2시간

        // 응답 설정: 헤더 key 값을 access로 설정
        response.setHeader("access", access);
        response.setStatus(HttpStatus.OK.value());

        // 프론트에 넘겨주기 위한 JSON 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"result\": \"로그인이 성공적으로 완료되었습니다.\"}");

        // 시큐리티 컨텍스트 홀더에 저장
        SecurityContextHolder.getContext().setAuthentication(authResult);

    }

    // 로그인 실패 시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

//        super.unsuccessfulAuthentication(request, response, failed);

        // 프론트에 넘겨주기 위한 JSON 응답
        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"result\": \"로그인에 실패하였습니다.\"}");
    }


}

