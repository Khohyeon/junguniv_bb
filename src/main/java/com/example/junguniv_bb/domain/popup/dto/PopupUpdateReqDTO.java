package com.example.junguniv_bb.domain.popup.dto;

import com.example.junguniv_bb.domain.popup.model.Popup;

public record PopupUpdateReqDTO(
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
        String contents
) {
    public Popup updateEntity() {
        return new Popup(
                popupIdx, popupName, startDate, endDate, widthSize, heightSize, topSize, leftSize, contents, chkToday,null, chkOpen, chkScrollbar
        );
    }
}
