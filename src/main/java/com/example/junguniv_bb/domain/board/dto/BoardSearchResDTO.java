package com.example.junguniv_bb.domain.board.dto;

import com.example.junguniv_bb.domain.board.model.BbsGroup;

public record BoardSearchResDTO(
        Long bbsIdx,
        BbsGroup bbsGroup,
        String title,
        String createdDate,
        Long readNum,
        String chkTopFix
) {
}
