package com.example.junguniv_bb.domain.auth.dto;

import com.example.junguniv_bb.domain.member.model.Member;

public record ResJoinDTO(MemberDTO member) {

    public ResJoinDTO(Member member) {
        this(new MemberDTO(member));
    }

    public record MemberDTO(Long memberIdx, String userId, String telMobile, String email, String userType) {
        public MemberDTO(Member member) {
            this(member.getMemberIdx(), member.getUserId(), member.getTelMobile(), member.getEmail(),
                    member.getUserType()
                            .getText());
        }
    }
}
