package com.example.junguniv_bb.domain.member.dto;

import com.example.junguniv_bb.domain.member.model.Member;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public record MemberTeacherPageResDTO(
        List<MemberDTO> memberList,
        PageableDTO pageable
) {
    public MemberTeacherPageResDTO(Page<Member> memberPage) {
        this(
                memberPage.getContent()
                        .stream()
                        .map(MemberDTO::new)
                        .toList(),
                new PageableDTO(memberPage)
        );
    }

    // 이름, userId, 휴대폰, 이메일, 첨삭 참여 건수, 회원등록일, 약관동의일
    public record MemberDTO(
            String name,
            String userId,
            String telMobile,
            String email,
            LocalDateTime createdDate,
            LocalDateTime agreeDate
    ) {
        public  MemberDTO (Member member) {
            this(
                    member.getName(),
                    member.getUserId(),
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
