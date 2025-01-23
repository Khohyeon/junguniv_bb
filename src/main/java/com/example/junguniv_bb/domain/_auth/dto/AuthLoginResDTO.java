package com.example.junguniv_bb.domain._auth.dto;

import com.example.junguniv_bb.domain.member.model.Member;

public record AuthLoginResDTO(TokenDTO token, MemberDTO member) {

    public AuthLoginResDTO(Member member, String accessToken, String refreshToken) {
        this(new TokenDTO(accessToken, refreshToken), new MemberDTO(member));
    }

    public record TokenDTO(String accessToken, String refreshToken) {
        // 생성자와 getter는 record가 자동으로 생성해줌
    }

    public record MemberDTO(
        Long memberIdx,          // 회원 IDX
        String userId,           // 아이디
        String name,            // 이름
        String userType,         // UserType enum의 name()을 직접 사용
        Long authLevel,         // 권한 레벨
        String memberState      // 계정 상태
    ) {
        public MemberDTO(Member member) {
            this(
                member.getMemberIdx(),
                member.getUserId(),
                member.getName(),
                member.getUserType().name(), // getText() 대신 name() 사용
                member.getAuthLevel(),
                member.getMemberState()
            );
        }
    }
}
