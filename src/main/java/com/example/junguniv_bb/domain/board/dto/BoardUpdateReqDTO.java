package com.example.junguniv_bb.domain.board.dto;

import com.example.junguniv_bb.domain.board.model.Bbs;
import com.example.junguniv_bb.domain.board.model.BbsGroup;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public record BoardUpdateReqDTO(
        Long bbsIdx,
        String boardType,
        String title,
//        String chksecret,
        String writer,
        String category,
//        String answerDate,
        String topFix,
        String fixStartDate,
        String fixEndDate,
        String chkMain,
        String startDate,
        String endDate,
        List<MultipartFile> attachments, // 첨부파일
        String contents, // 에디터 내용
        String recipientName,
        String recipientId,
        String pwd
) {

    public Bbs updateEntity(BbsGroup bbsGroupIdx) {
        return new Bbs(
                bbsIdx, // ID는 자동 생성
                pwd,
                bbsGroupIdx,
                boardType,
                title,
                writer,
                category,
                topFix == null ? "N" : topFix,
                parseDate(fixStartDate),
                parseDate(fixEndDate),
                chkMain,
                parseDate(startDate),
                parseDate(endDate),
                contents,
                recipientName,
                recipientId
        );
    }

    private LocalDate parseDate(String date) {
        return (date != null && !date.isEmpty()) ? LocalDate.parse(date) : null;
    }
}
