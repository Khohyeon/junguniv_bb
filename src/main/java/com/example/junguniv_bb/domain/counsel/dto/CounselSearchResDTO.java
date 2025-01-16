package com.example.junguniv_bb.domain.counsel.dto;

public record CounselSearchResDTO(
        Long counselIdx,
        String counselName,
        String createdDate,
        String name,
        Integer counselState,
        String updateDate
) {
}
