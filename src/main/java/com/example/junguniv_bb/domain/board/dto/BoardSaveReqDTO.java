package com.example.junguniv_bb.domain.board.dto;

import com.example.junguniv_bb.domain.board.model.Bbs;
import com.example.junguniv_bb.domain.board.model.BbsGroup;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public record BoardSaveReqDTO(
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
    public Bbs saveEntity(BbsGroup bbsGroupIdx, String name) {
        return new Bbs(
                null, // ID는 자동 생성
                pwd,
                bbsGroupIdx,
                boardType,
                title,
                name == null ? "로그인한 유저이름" : name,
                category,
                topFix == null ? "N" : topFix,
                parseDate(fixStartDate),
                parseDate(fixEndDate),
                chkMain,
                parseDate(startDate),
                parseDate(endDate),
                contents,
                recipientName,
                recipientId,
                null,
                0L
        );
    }

    private LocalDate parseDate(String date) {
        return (date != null && !date.isEmpty()) ? LocalDate.parse(date) : null;
    }
}
