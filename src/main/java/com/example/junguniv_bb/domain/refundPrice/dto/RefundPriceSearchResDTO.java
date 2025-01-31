package com.example.junguniv_bb.domain.refundPrice.dto;

public record RefundPriceSearchResDTO(
        Long refundPriceIdx,
        String refundPriceType,
        String refundPriceName,
        Long refundRate,
        String chkUse,
        String discountType,
        Long sortno,
        String studyType
) {
}
