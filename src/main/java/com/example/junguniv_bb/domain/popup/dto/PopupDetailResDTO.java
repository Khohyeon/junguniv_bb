package com.example.junguniv_bb.domain.popup.dto;

public record PopupDetailResDTO(
        Long popupIdx,
        String popupName,
        String startDate,
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
        String bgcolor
) {
}
