package com.example.junguniv_bb.domain.board.dto;

public record BbsGroupPageResDTO(
        Long bbsGroupIdx,
        String bbsId,
        String bbsGroupName,
        String category,
        Integer fileNum,
        String skin,
        String optionSecretAuth,
        String optionReplyAuth,
        String optionCommentAuth,
        String readAuth,    // @guest@member 형태로 저장
        String writeAuth,
        String commentAuth,
        String replyAuth
) {
}
