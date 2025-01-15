package com.example.junguniv_bb.domain.popup.dto;

public record PopupPageResDTO(
        Long popupIdx,
        String popupName,
        String updateDate,
        String startDate,
        String endDate,
        String popupType,
        String chkOpen
) {
}
