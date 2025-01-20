package com.example.junguniv_bb.domain.member.dto;

import com.example.junguniv_bb.domain.member.model.Member;
import org.springframework.data.domain.Page;

import java.util.List;

public record MemberAddressPageResDTO(
    List<MemberDTO> memberList,
    PageableDTO pageable
) {
    public MemberAddressPageResDTO(Page<Member> memberPage) {
        this(
            memberPage.getContent()
                    .stream()
                    .map(MemberDTO::new)
                    .toList(),
            new PageableDTO(memberPage)
        );
    }

    public record MemberDTO(
        Long memberIdx,
        String name,
        String userId,
        String telMobile,
        String zipcode,
        String addr1,
        String addr2
    ) {
        public MemberDTO(Member member) {
            this(
                member.getMemberIdx(),
                member.getName(),
                member.getUserId(),
                member.getTelMobile(),
                member.getZipcode(),
                member.getAddr1(),
                member.getAddr2()
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