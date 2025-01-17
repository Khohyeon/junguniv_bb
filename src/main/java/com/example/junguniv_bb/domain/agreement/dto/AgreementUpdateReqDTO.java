package com.example.junguniv_bb.domain.agreement.dto;

import com.example.junguniv_bb.domain.agreement.model.Agreement;

public record AgreementUpdateReqDTO(
        Long agreementIdx,
        String agreementContents,
        String agreementType
) {
    public Agreement updateEntity() {
        return new Agreement(
                agreementIdx, agreementContents, agreementType, "Y"
        );
    }

    public Agreement saveEntity() {
        return new Agreement(
                agreementIdx, agreementContents, agreementType, "Y"
        );
    }
}
