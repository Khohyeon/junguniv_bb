package com.example.junguniv_bb.domain.member.dto;

import com.example.junguniv_bb.domain.member.model.Member;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record MemberAdminPageResDTO(
        List<MemberDTO> memberList,
        PageableDTO pageable
) {
    public MemberAdminPageResDTO(Page<Member> memberPage, Map<Long, String> authLevelNames) {
        this(
                memberPage.getContent()
                        .stream()
                        .map(member -> new MemberDTO(
                                member,
                                authLevelNames.getOrDefault(member.getAuthLevel(), "-")
                        ))
                        .toList(),
                new PageableDTO(memberPage)
        );
    }

    // 이름, userId, 휴대폰, 이메일, 환급/일반 담당, 관리자 권한명, 근무상태, 회원등록일
    public record MemberDTO(
            Long memberIdx, // 회원 IDX
            String name, // 이름
            String userId, // 아이디
            String telMobile, // 휴대폰
            String email, // 이메일
            String jobCourseDuty, // 환급/일반 담당
            Long authLevel, // 권한명
            String authLevelName, // 권한명 추가
            String jobWorkState, // 근무상태
            LocalDateTime createdDate // 회원등록일
    ) {
        public MemberDTO(Member member, String authLevelName) {
            this(
                    member.getMemberIdx(),
                    member.getName(),
                    member.getUserId(),
                    member.getTelMobile(),
                    member.getEmail(),
                    member.getJobCourseDuty(),
                    member.getAuthLevel(),
                    authLevelName, // 권한명 전달
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
