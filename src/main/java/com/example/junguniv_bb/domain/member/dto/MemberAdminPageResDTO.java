package com.example.junguniv_bb.domain.member.dto;

import com.example.junguniv_bb.domain.member.model.Member;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public record MemberAdminPageResDTO(
        List<MemberDTO> memberList,
        PageableDTO pageable
) {
    public MemberAdminPageResDTO(Page<Member> memberPage) {
        this(
                memberPage.getContent()
                        .stream()
                        .map(MemberDTO::new)
                        .toList(),
                new PageableDTO(memberPage)
        );
    }

    // 이름, userId, 휴대폰, 이메일, 환급/일반 담당, 관리자 권한명, 근무상태, 회원등록일
    public record MemberDTO(
            Long memberIdx,
            String name, // 이름
            String userId, // 아이디
            String telMobile, // 휴대폰
            String email, // 이메일
            String jobDuty, // 환급/일반 담당
            Long authLevel, // 권한명
            String jobWorkState, // 근무상태
            LocalDateTime createdDate // 회원등록일
    ) {
        public  MemberDTO (Member member) {
            this(
                    member.getMemberIdx(),
                    member.getName(),
                    member.getUserId(),
                    member.getTelMobile(),
                    member.getEmail(),
                    member.getJobDuty(),
                    member.getAuthLevel(),
                    member.getJobWorkState(),
                    member.getCreatedDate()
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
