package com.example.junguniv_bb.domain.board.dto;

public record BoardCommentDetailResDTO(
        Long bbsIdx,
        Long commentIdx,
        String writer,
        String contents,
        String createdDate
) {
}
