package com.example.junguniv_bb.domain.board.dto;

import java.time.LocalDate;
import java.util.List;

public record BoardUpdateResDTO(
        Long bbsIdx,
        String title,
        String writer,
        String category,
        String createdDate,
        String topFix,
        LocalDate fixStartDate,
        LocalDate fixEndDate,
        String chkMain,
        LocalDate startDate,
        LocalDate endDate,
        String contents,
        String recipientName,
        String recipientId,
        List<String> attachments // 첨부파일 이름 리스트
) {
}
