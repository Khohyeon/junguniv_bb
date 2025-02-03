package com.example.junguniv_bb.domain.survey.dto;

import com.example.junguniv_bb.domain.survey.model.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public record SurveyPageResDTO(List<SurveyDTO> surveyList, PageableDTO pageable) {

    public static SurveyPageResDTO from(Page<Survey> surveyPage) {
        return new SurveyPageResDTO(
            surveyPage.getContent().stream()
                .map(SurveyDTO::from)
                .collect(Collectors.toList()),
            new PageableDTO(surveyPage)
        );
    }

    public record SurveyDTO(
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
        public static SurveyDTO from(Survey survey) {
            return new SurveyDTO(
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

    public record PageableDTO(int pageNumber, int pageSize, int totalPages, long totalElements, boolean isLast,
            int numberOfElements, boolean isEmpty, Sort sort) {
        public PageableDTO(Page<Survey> surveyPage) {
            this(surveyPage.getNumber(), surveyPage.getSize(), surveyPage.getTotalPages(),
                    surveyPage.getTotalElements(), surveyPage.isLast(), surveyPage.getNumberOfElements(),
                    surveyPage.isEmpty(), surveyPage.getSort());
        }
    }
}
