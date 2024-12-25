package com.pedalgenie.vote.domain.jwt.filter;

import com.pedalgenie.vote.domain.auth.CustomUserDetails;
import com.pedalgenie.vote.domain.auth.CustomUserDetailsService;
import com.pedalgenie.vote.domain.jwt.JwtUtil;
import com.pedalgenie.vote.domain.jwt.TokenValidator;
import com.pedalgenie.vote.domain.member.entity.Member;
import com.pedalgenie.vote.domain.member.repostiory.MemberRepository;
import com.pedalgenie.vote.global.exception.CustomException;
import com.pedalgenie.vote.global.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenValidator tokenValidator;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 final FilterChain filterChain)
        throws ServletException, IOException{

        // 헤더에서 access 키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("access");

        // 토큰이 없다면 다음 필터로 넘김
        if(!StringUtils.hasText(accessToken)){
            filterChain.doFilter(request,response);
            return;
        }
        // 토큰 만료 여부 확인, 만료 시 다음 필터로 넘기지 않음
        tokenValidator.validateTokenExpired(accessToken);

        // 인증 정보 저장
        setAuthentication(accessToken);

        filterChain.doFilter(request, response);

    }

    // jwt 토큰 사용해서 사용자 인증, SecurityContext 에 인증 정보 설정
    private void setAuthentication(String accessToken){

        // 토큰에서 유저 이름 추출
        String username = jwtUtil.getUsername(accessToken);

        // 유저 디비에서 이름 찾기
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXISTS_MEMBER_NAME));

        // 해당 유저 정보 로드
        CustomUserDetails userDetails = new CustomUserDetails(member);

        // 인증 객체 생성(principal, credentials), 비밀번호는 인증 후 더 이상 필요하지 않으므로 null
        Authentication authToken =
                new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());

        // 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
