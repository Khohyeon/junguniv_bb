package com.example.junguniv_bb.domain.member.dto;

import com.example.junguniv_bb.domain.member.model.Member;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public record MemberCompanyPageResDTO(
        List<MemberDTO> memberList,
        PageableDTO pageable
) {
    public MemberCompanyPageResDTO(Page<Member> memberPage) {
        this(
                memberPage.getContent()
                        .stream()
                        .map(MemberDTO::new)
                        .toList(),
                new PageableDTO(memberPage)
        );
    }

    // 위탁기업명, userId, 교육담당자, 교육담당자 휴대폰, 교육담당자 이메일, 소속 수강생 수, 대표자명
    public record MemberDTO(
            Long memberIdx, // 회원 IDX
            String jobName, // 위탁기업명
            String userId, // 아이디
            String contractorName, // 교육담당자
            String contractorTel, // 교육담당자 휴대폰
            String contractorEtc, // 교육담당자 이메일
            String jobCeo, // 대표자명
            String jobRegion, // 지역
            String jobScale, // 기업규모
            String jobNumber, // 사업자번호
            String jobInsuranceNumber // 사업자등록번호

    ) {
        public  MemberDTO (Member member) {
            this(
                    member.getMemberIdx(),
                    member.getJobName(),
                    member.getUserId(),
                    member.getContractorName(),
                    member.getContractorTel(),
                    member.getContractorEtc(),
                    member.getJobCeo(),
                    member.getJobRegion(),
                    member.getJobScale(),
                    member.getJobNumber(),
                    member.getJobInsuranceNumber()
            );
        }
    }

    public record PageableDTO(
            int pageNumber,
            int pageSize,
            int totalPages,
            long totalElements,
            boolean isLast,
            int numberOfElements,
            boolean isEmpty
    ) {
        public PageableDTO(Page<?> page) {
            this(
                    page.getNumber(),
                    page.getSize(),
                    page.getTotalPages(),
                    page.getTotalElements(),
                    page.isLast(),
                    page.getNumberOfElements(),
                    page.isEmpty()
            );
        }
    }
}
