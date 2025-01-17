package com.example.junguniv_bb.domain.member.dto;

public record MemberAdminSearchReqDTO(
    String name,              // 이름
    String userId,            // 아이디
    String jobCourseDuty     // 환급/일반 담당
) {} 