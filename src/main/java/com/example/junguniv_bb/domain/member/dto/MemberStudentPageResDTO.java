package com.example.junguniv_bb.domain.member.dto;

import com.example.junguniv_bb.domain.member.model.Member;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public record MemberStudentPageResDTO(
        List<MemberDTO> memberList,
        PageableDTO pageable
) {
    public MemberStudentPageResDTO(Page<Member> memberPage) {
        this(
                memberPage.getContent()
                        .stream()
                        .map(MemberDTO::new)
                        .toList(),
                new PageableDTO(memberPage)
        );
    }

    // 이름, userId, 생년월일, 휴대폰, 이메일, 마케팅 문자 수신, 메일 수신, 회원등록일, 약관동의일
    public record MemberDTO(
            Long memberIdx, // 회원 IDX
            String name, // 이름
            String userId, // 아이디
            String birthday, // 생년월일
            String telMobile, // 휴대폰
            String email, // 이메일
            String chkMailReceive, // 메일 수신
            String chkSmsReceive, // 마케팅 문자 수신
            LocalDateTime createdDate, // 회원등록일
            LocalDateTime agreeDate // 약관동의일
    ) {
        public  MemberDTO (Member member) {
            this(
                    member.getMemberIdx(),
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
