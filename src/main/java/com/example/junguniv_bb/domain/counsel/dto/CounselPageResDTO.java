package com.example.junguniv_bb.domain.counsel.dto;

public record CounselPageResDTO(
        Long counselIdx,
        String memo,
        String talkTime,
        String name,
        Integer counselState,
        String updateDate
) {
}
