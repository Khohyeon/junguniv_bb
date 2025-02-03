package com.example.junguniv_bb.domain.survey.dto;

import com.example.junguniv_bb.domain.survey.model.Survey;

public record SurveyDetailResDTO(
    Long surveyIdx,        // 강의(설문)평가 IDX
    String surveyTitle,    // 강의(설문)평가 제목
    String surveyTarget,   // 강의(설문)평가 대상
    String chkOpen,        // 공개 여부
    Long subjectIdx,       // 과정 IDX
    String subjectName,    // 과정명
    String chkNames,       // 의무차시
    String chkRequire,     // 필수체크
    String surveyViewPoint // 실시지점
) {
    public static SurveyDetailResDTO from(Survey survey) {
        return new SurveyDetailResDTO(
            survey.getSurveyIdx(),
            survey.getSurveyTitle(),
            survey.getSurveyTarget(),
            survey.getChkOpen(),
            survey.getSubjectIdx(),
            survey.getSubjectName(),
            survey.getChkNames(),
            survey.getChkRequire(),
            survey.getSurveyViewPoint()
        );
    }
}
