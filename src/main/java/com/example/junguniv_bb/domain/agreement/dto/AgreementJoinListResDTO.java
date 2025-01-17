package com.example.junguniv_bb.domain.agreement.dto;

public record AgreementJoinListResDTO(
        Long agreementIdx,
        String trainingCenterName,
        String trainingCenterUrl,
        String agreementTitle,
        String agreementContents,
        String openYn

) {
}
