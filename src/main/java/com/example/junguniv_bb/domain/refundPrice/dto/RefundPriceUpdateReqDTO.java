package com.example.junguniv_bb.domain.refundPrice.dto;

import com.example.junguniv_bb.domain.refundPrice.model.RefundPrice;
import jakarta.validation.constraints.*;

public record RefundPriceUpdateReqDTO(
        Long refundPriceIdx,
//        @NotBlank(message = "환불 유형을 입력하세요.") // 빈 값 또는 공백 체크
        String refundPriceType,

        @NotBlank(message = "환불 항목명을 입력하세요.") // 빈 값 또는 공백 체크
        String refundPriceName,

        @NotBlank(message = "할인 종류를 선택하세요.") // 빈 값 또는 공백 체크
        @Pattern(regexp = "percent|discount", message = "할인 종류는 'percent' 또는 'discount'만 가능합니다.")
        String discountType,

        @NotNull(message = "할인율 또는 할인 금액을 입력하세요.") // null 체크
        @Min(value = 0, message = "할인율/할인 금액은 0 이상이어야 합니다.")
        Long refundRate,

        @NotBlank(message = "사용 여부를 선택하세요.") // 빈 값 또는 공백 체크
        @Pattern(regexp = "Y|N", message = "사용 여부는 'Y' 또는 'N'만 가능합니다.")
        String chkUse,

        @NotNull(message = "정렬 번호를 입력하세요.") // null 체크
        @Min(value = 1, message = "정렬 번호는 1 이상이어야 합니다.")
        Long sortno,
        String studyType
) {

    public RefundPrice updateEntity() {
        return new RefundPrice(
                refundPriceIdx, refundPriceType, refundPriceName, discountType, refundRate, chkUse, sortno, studyType
        );
    }

    @AssertTrue(message = "할인 종류가 '지원율'일 경우, 지원율은 100 이하이어야 합니다.")
    public boolean isValidRefundRate() {
        if ("percent".equals(discountType) && refundRate != null) {
            return refundRate <= 100; // ✅ percent이면 100 이하
        }
        return true; // discount이면 제한 없음
    }
}