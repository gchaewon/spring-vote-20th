package com.pedalgenie.vote.global.config;

import com.pedalgenie.vote.domain.jwt.filter.JwtAuthenticationFilter;
import com.pedalgenie.vote.domain.jwt.JwtUtil;
import com.pedalgenie.vote.domain.jwt.filter.JwtValidationFilter;
import com.pedalgenie.vote.domain.jwt.TokenValidator;
import com.pedalgenie.vote.domain.member.repostiory.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final TokenValidator tokenValidator;
    private final MemberRepository memberRepository;

    // 허용된 URI 목록
    private static final List<String> ALLOWED_URIS = Arrays.asList(
            "/swagger-ui/index.html",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/api/**",
            "http://localhost:3000/**"
    );

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable);

        http
                // 접근 허용된 URI
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ALLOWED_URIS.toArray(new String[0])).permitAll()
                        .anyRequest().authenticated());

        // AuthenticationManager 생성
        AuthenticationManager authenticationManager = authenticationManager(authenticationConfiguration);

        // JwtAuthenticationFilter 설정
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtUtil,memberRepository);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/login"); // 로그인 URL 설정

        // 필터 추가
        http
                .addFilterBefore(new JwtValidationFilter(jwtUtil, tokenValidator,memberRepository), JwtAuthenticationFilter.class)
                .addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http
                .sessionManagement((session)->session.sessionCreationPolicy(STATELESS));

        return http.build();
    }


    protected CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", getDefaultCorsConfiguration());
        return source;
    }

    private CorsConfiguration getDefaultCorsConfiguration() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://localhost:8080");
        // 프론트 배포 url 추가 필요
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        return config;
    }

    // AuthenticationManager 빈 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
