package com.example.junguniv_bb.domain.agreement.dto;

import com.example.junguniv_bb.domain.agreement.model.Agreement;

public record AgreementJoinUpdateReqDTO(
        Long agreementIdx,
        String trainingCenterName,
        String trainingCenterUrl,
        String agreementTitle,
        String agreementContents
) {
    public Agreement updateJoinEntity() {
        return new Agreement(
                agreementIdx, trainingCenterName, trainingCenterUrl, agreementTitle, agreementContents, "Y", "JOIN"
        );
    }
}
