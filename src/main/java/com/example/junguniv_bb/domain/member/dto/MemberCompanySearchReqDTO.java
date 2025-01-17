package com.example.junguniv_bb.domain.member.dto;

public record MemberCompanySearchReqDTO(
    String jobName,           // 기업명
    String userId,            // ID
    String jobNumber,         // 사업자번호
    String contractorName,    // 교육담당자
    String contractorTel,     // 연락처
    String contractorEtc,     // 이메일
    String jobScale          // 기업규모
) {} 