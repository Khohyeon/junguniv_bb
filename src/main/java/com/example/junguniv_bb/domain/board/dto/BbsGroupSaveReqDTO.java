package com.example.junguniv_bb.domain.board.dto;

import com.example.junguniv_bb.domain.board.model.BbsGroup;

public record BbsGroupSaveReqDTO(
        String bbsGroupName,
        String bbsId
) {
    public BbsGroup saveEntity() {
        return new BbsGroup(
                null, bbsGroupName, bbsId
        );
    }
}
