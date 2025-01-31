package com.example.junguniv_bb.domain.refundPrice.dto;

public record RefundPriceDetailResDTO(
        Long refundPriceIdx,
        String refundPriceType,
        String refundPriceName,
        String discountType,
        Long refundRate,
        String chkUse,
        Long sortno
) {
}
