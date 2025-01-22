package com.example.junguniv_bb.domain.board.dto;

import com.example.junguniv_bb.domain.board.model.Bbs;
import com.example.junguniv_bb.domain.board.model.BbsComment;
import com.example.junguniv_bb.domain.member.model.Member;

public record BoardCommentSaveReqDTO(
        Long bbsIdx,
        String contents,
        String boardType
) {

    public BbsComment saveEntity(Bbs bbs, Member member) {
        return new BbsComment(
                null,
                bbs,
                member.getName(),
                null,
                contents,
                null,
                null,
                member.getUserId(),
                member.getUserId()
        );
    }
}
