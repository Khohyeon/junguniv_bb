package com.example.junguniv_bb.domain.agreement.dto;

import com.example.junguniv_bb.domain.agreement.model.Agreement;

public record AgreementJoinUpdateReqDTO(
        Long agreementIdx,
        String agreementTitle,
        String agreementContents,
        String openYn
) {
    public Agreement updateJoinEntity(String trainingCenterName, String trainingCenterUrl) {
        return new Agreement(
                agreementIdx, agreementTitle, trainingCenterName, trainingCenterUrl, agreementContents, openYn, "JOIN"
        );
    }
}
