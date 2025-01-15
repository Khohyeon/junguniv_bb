package com.example.junguniv_bb._core.security;

import com.example.junguniv_bb.domain.member.model.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    
    private final Member member;
    private Long currentMenuIdx; // 현재 접근 중인 메뉴 IDX
    private boolean hasPrivacyAccess; // 개인정보 접근 권한 여부

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    // 현재 메뉴 IDX 설정
    public void setCurrentMenuIdx(Long menuIdx) {
        this.currentMenuIdx = menuIdx;
    }

    // 현재 메뉴 IDX 조회
    public Long getCurrentMenuIdx() {
        return this.currentMenuIdx;
    }

    // 멤버 정보 반환
    public Member getMember() {
        return this.member;
    }

    // 권한 레벨 반환
    public Long getAuthLevel() {
        return member.getAuthLevel();
    }

    public void setHasPrivacyAccess(boolean hasPrivacyAccess) {
        this.hasPrivacyAccess = hasPrivacyAccess;
    }

    public boolean hasPrivacyAccess() {
        return this.hasPrivacyAccess;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> member.getUserType().name());
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPwd();
    }

    @Override
    public String getUsername() {
        return member.getUserId(); // 회원ID로 설정
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
        // memberState가 N이면 false 반환하여 계정 비활성화 처리
        return !member.getMemberState().equals("N");
    }
}