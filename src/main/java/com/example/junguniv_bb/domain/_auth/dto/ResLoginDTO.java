package com.example.junguniv_bb.domain._auth.dto;

import com.example.junguniv_bb.domain.member.model.Member;

public record ResLoginDTO(TokenDTO token, MemberDTO member) {

    public ResLoginDTO(Member member, String accessToken, String refreshToken) {
        this(new TokenDTO(accessToken, refreshToken), new MemberDTO(member));
    }

    public record TokenDTO(String accessToken, String refreshToken) {
        // 생성자와 getter는 record가 자동으로 생성해줌
    }

    public record MemberDTO(Long memberIdx, String userId, String telMobile, String email, String userType) {
        public MemberDTO(Member member) {
            this(member.getMemberIdx(), member.getUserId(), member.getTelMobile(), member.getEmail(),
                    member.getUserType()
                            .getText());
        }
    }
}
