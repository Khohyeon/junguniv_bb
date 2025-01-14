package com.example.junguniv_bb.domain.member.dto;

import com.example.junguniv_bb.domain.member.model.Member;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public record MemberPageResDTO(
        List<MemberDTO> memberList,
        PageableDTO pageable
) {
    public MemberPageResDTO(Page<Member> memberPage) {
        this(
                memberPage.getContent()
                        .stream()
                        .map(MemberDTO::new)
                        .toList(),
                new PageableDTO(memberPage)
        );
    }

    // 이름, userId, 생년월일, 핸드폰, 이메일, 마케팅 문자 수신, 메일 수신, 회원등록일, 약관동의일
    public record MemberDTO(
            String name,
            String userId,
            String birthday,
            String telMobile,
            String email,
            String chkMailReceive,
            String chkSmsReceive,
            LocalDateTime createdDate,
            LocalDateTime agreeDate
    ) {
        public  MemberDTO (Member member) {
            this(
                    member.getName(),
                    member.getUserId(),
                    member.getBirthday(),
                    member.getTelMobile(),
                    member.getEmail(),
                    member.getChkMailReceive(),
                    member.getChkSmsReceive(),
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
