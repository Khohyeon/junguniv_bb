package com.example.junguniv_bb.domain.member.dto;

import com.example.junguniv_bb.domain.member.model.Member;

public record MemberAddressPageResDTO(
        Long memberIdx,          // 회원 인덱스
        String name,            // 이름
        String userId,          // 아이디
        String telMobile,       // 휴대폰 번호
        String zipcode,         // 우편번호
        String addr1,           // 기본 주소
        String addr2           // 상세 주소
) {
    // Member 엔티티로부터 DTO를 생성하는 정적 팩토리 메서드
    public static MemberAddressPageResDTO from(Member member) {
        return new MemberAddressPageResDTO(
            member.getMemberIdx(),
            member.getName(),
            member.getUserId(),
            member.getTelMobile(),
            member.getZipcode(),
            member.getAddr1(),
            member.getAddr2()
        );
    }
}