package com.pedalgenie.vote.domain.auth;

import com.pedalgenie.vote.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member;

    // 권한 객체 목록을 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        // 권한 목록
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // 권한 추가
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }
    @Override
    public String getPassword(){
        return member.getPassword();
    }
    @Override
    public String getUsername() {
        return member.getLoginId(); // 로그인 아이디 리턴
    }

    // memberId 얻는 메서드 추가
    public Long getMemberId(){
        return member.getMemberId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
