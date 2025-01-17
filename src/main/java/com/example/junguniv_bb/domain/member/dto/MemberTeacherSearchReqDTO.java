package com.example.junguniv_bb.domain.member.dto;

public record MemberTeacherSearchReqDTO(
    String name,                    // 이름
    String userId,                  // 아이디
    String jobEmployeeType,         // 근무형태
    String telMobile,               // 휴대폰
    String email                    // 이메일
) {} 