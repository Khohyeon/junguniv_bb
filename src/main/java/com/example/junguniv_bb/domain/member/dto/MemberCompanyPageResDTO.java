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

    // 위탁기업명, userId, 교육담당자, 교육담당자 휴대폰, 교육담당자 이메일, 소속 수강생 수, 대표자명, 기업전용 페이지
    public record MemberDTO(
            String jobName,
            String userId,
            String name,
            String telMobile,
            String email,
            LocalDateTime createdDate,
            LocalDateTime agreeDate
    ) {
        public  MemberDTO (Member member) {
            this(
                    member.getJobName(),
                    member.getUserId(),
                    member.getName(),
                    member.getTelMobile(),
                    member.getEmail(),
                    member.getCreatedDate(),
                    member.getAgreeDate()
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
