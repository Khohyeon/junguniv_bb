package com.example.junguniv_bb.domain.member.dto;

import com.example.junguniv_bb.domain.member.model.Member;

public record MemberAddressLabelDTO(
    String name,            // 이름
    String zipcode,         // 우편번호
    String addr1,           // 기본주소
    String addr2,           // 상세주소
    String jobName,         // 회사명
    String jobDept,         // 부서
    String telMobile       // 휴대폰
) {
    public static MemberAddressLabelDTO from(Member member) {
        return new MemberAddressLabelDTO(
            member.getName(),
            member.getZipcode(),
            member.getAddr1(),
            member.getAddr2(),
            member.getJobName(),
            member.getJobDept(),
            member.getTelMobile()
        );
    }
} 