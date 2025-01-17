package com.example.junguniv_bb.domain.board.dto;

import com.example.junguniv_bb.domain.board.model.BbsGroup;

public record BbsGroupUpdateReqDTO(
        Long bbsGroupIdx,
        String bbsGroupName,
        String bbsId,
        String category,
        Integer fileNum,
        String skin,
        String optionSecretAuth,
        String optionReplyAuth,
        String optionCommentAuth,
        String readAuth,
        String writeAuth,
        String commentAuth,
        String replyAuth
) {

    public BbsGroup updateEntity() {
        return new BbsGroup(
                bbsGroupIdx, bbsGroupName, bbsId, skin, category, fileNum, readAuth, writeAuth, replyAuth, commentAuth, optionSecretAuth, optionReplyAuth, optionCommentAuth, null
        );
    }
}
