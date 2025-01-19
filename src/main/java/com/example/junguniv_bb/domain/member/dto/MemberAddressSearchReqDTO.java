package com.example.junguniv_bb.domain.member.dto;

public record MemberAddressSearchReqDTO(
        String name,            // 이름
        String userId,          // 아이디
        String address,         // 주소
        String telMobile,       // 휴대폰 번호
        String email,           // 이메일
        String jobName          // 근무회사
) {}
