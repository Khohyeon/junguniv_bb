package com.example.junguniv_bb.domain.board.dto;

import java.util.List;

public record BoardDetailResDTO(
        Long bbsIdx,
        String title,
        String writer,
        String createdDate,
        Long readNum,
        String contents,
        boolean optionCommentAuth,
        boolean optionReplyAuth,
        List<String> attachments // 첨부파일 이름 리스트
){
}
