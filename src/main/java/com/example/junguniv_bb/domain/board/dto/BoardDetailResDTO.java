package com.example.junguniv_bb.domain.board.dto;

public record BoardDetailResDTO(
        Long bbsIdx,
        String title,
        String writer,
        String createdDate,
        Long readNum,
        String contents
){
}
