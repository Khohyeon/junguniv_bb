package com.example.junguniv_bb.domain.popup.dto;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ValidExceptionMessage;
import com.example.junguniv_bb._core.util.DateValidator;
import com.example.junguniv_bb.domain.popup.model.Popup;
import jakarta.validation.constraints.NotEmpty;

public record PopupUpdateReqDTO(
        Long popupIdx,
        String popupName,
        @NotEmpty(message = ValidExceptionMessage.Message.INVALID_DATE_FORMAT)
        String startDate,
        @NotEmpty(message = ValidExceptionMessage.Message.INVALID_DATE_FORMAT)
        String endDate,
        String chkOpen,
        String chkToday,
        String chkScrollbar,
        String widthSize,
        String heightSize,
        String leftSize,
        String topSize,
        String contents,
        String popupUrl,
        String bgcolor,
        String fname1,
        String fname1Name
) {
    public PopupUpdateReqDTO {
        if (!DateValidator.isValidDate(startDate)) {
            throw new Exception400("시작 날짜 형식이 올바르지 않습니다: " + startDate);
        }
        if (!DateValidator.isValidDate(endDate)) {
            throw new Exception400("종료 날짜 형식이 올바르지 않습니다: " + endDate);
        }
        if (!DateValidator.isValidDateRange(startDate, endDate)) {
            throw new Exception400("시작 날짜는 종료 날짜보다 이후일 수 없습니다.");
        }
    }

    public Popup updateEntity() {
        return new Popup(
                popupIdx, popupName, startDate, endDate, widthSize, heightSize, topSize, leftSize, contents, chkToday,null, chkOpen, chkScrollbar, popupUrl, bgcolor, fname1, fname1Name
        );
    }
}
