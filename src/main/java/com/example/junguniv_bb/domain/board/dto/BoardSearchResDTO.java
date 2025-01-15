package com.example.junguniv_bb.domain.board.dto;

public record BoardSearchResDTO(
        Long bbsIdx,
        Long bbsGroupIdx,
        String title,
        String createdDate,
        Long readNum
) {
}
