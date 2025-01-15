package com.example.junguniv_bb.domain.agreement.dto;

import com.example.junguniv_bb.domain.agreement.model.Agreement;

public record AgreementUpdateReqDTO(
        Long agreementIdx,
        String agreementTitle,
        String agreementContents
) {
    public Agreement updateEntity() {
        return new Agreement(
                agreementIdx, agreementTitle, agreementContents, "Y"
        );
    }
}
