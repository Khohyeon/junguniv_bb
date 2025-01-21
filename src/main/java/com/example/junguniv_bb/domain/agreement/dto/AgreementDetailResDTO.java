package com.example.junguniv_bb.domain.agreement.dto;

public record AgreementDetailResDTO(
        Long agreementIdx,
        String agreementTitle,
        String agreementContents,
        String openYn
) {
}
