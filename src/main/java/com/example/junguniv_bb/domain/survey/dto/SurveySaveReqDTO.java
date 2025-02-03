package com.example.junguniv_bb.domain.survey.dto;

import com.example.junguniv_bb.domain.survey.model.Survey;
import jakarta.validation.constraints.Size;

public record SurveySaveReqDTO(
    @Size(max = 255, message = "강의(설문)평가 제목은 255자를 초과할 수 없습니다")
    String surveyTitle, // 강의(설문)평가 제목

    String surveyTarget, // 강의(설문)평가 대상 (mediumtext)

    @Size(max = 1, message = "공개 여부는 1자를 초과할 수 없습니다")
    String chkOpen, // 공개 여부

    Long subjectIdx, // 과정 IDX

    @Size(max = 255, message = "과정명은 255자를 초과할 수 없습니다")
    String subjectName, // 과정명

    @Size(max = 10, message = "의무차시는 10자를 초과할 수 없습니다")
    String chkNames, // 의무차시

    @Size(max = 1, message = "필수체크는 1자를 초과할 수 없습니다")
    String chkRequire, // 필수체크

    @Size(max = 50, message = "실시지점은 50자를 초과할 수 없습니다")
    String surveyViewPoint // 실시지점
) {
    public Survey toEntity() {
        return Survey.builder()
            .surveyTitle(surveyTitle)
            .surveyTarget(surveyTarget)
            .chkOpen(chkOpen)
            .subjectIdx(subjectIdx)
            .subjectName(subjectName)
            .chkNames(chkNames)
            .chkRequire(chkRequire)
            .surveyViewPoint(surveyViewPoint)
            .build();
    }
}