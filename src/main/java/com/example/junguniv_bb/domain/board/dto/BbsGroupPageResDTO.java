package com.example.junguniv_bb.domain.board.dto;

public record BbsGroupPageResDTO(
        Long bbsGroupIdx,
        String bbsId,
        String bbsGroupName,
        Integer fileNum
) {
}
