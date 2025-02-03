package com.example.junguniv_bb.domain.survey._quest.dto;

import com.example.junguniv_bb.domain.survey._quest.model.SurveyQuest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public record SurveyQuestPageResDTO(List<SurveyQuestDTO> surveyQuestList, PageableDTO pageable) {

    public static SurveyQuestPageResDTO from(Page<SurveyQuest> surveyQuestPage) {
        return new SurveyQuestPageResDTO(
            surveyQuestPage.getContent().stream()
                .map(SurveyQuestDTO::from)
                .collect(Collectors.toList()),
            new PageableDTO(surveyQuestPage)
        );
    }

    public record SurveyQuestDTO(
        Long questIdx,        // 세부항목 IDX
        String questType,     // 평가유형
        String questContent,  // 질문내용
        Long surveyIdx,       // 강의(설문)평가 IDX
        Long sortNo,          // 정렬값
        String questRequired, // 필수여부
        String questSubType   // 평가유형(단일, 다중선택)
    ) {
        public static SurveyQuestDTO from(SurveyQuest surveyQuest) {
            return new SurveyQuestDTO(
                surveyQuest.getQuestIdx(),
                surveyQuest.getQuestType(),
                surveyQuest.getQuestContent(),
                surveyQuest.getSurveyIdx(),
                surveyQuest.getSortNo(),
                surveyQuest.getQuestRequired(),
                surveyQuest.getQuestSubType()
            );
        }
    }

    public record PageableDTO(int pageNumber, int pageSize, int totalPages, long totalElements, boolean isLast,
            int numberOfElements, boolean isEmpty, Sort sort) {
        public PageableDTO(Page<SurveyQuest> surveyQuestPage) {
            this(surveyQuestPage.getNumber(), surveyQuestPage.getSize(), surveyQuestPage.getTotalPages(),
                    surveyQuestPage.getTotalElements(), surveyQuestPage.isLast(), surveyQuestPage.getNumberOfElements(),
                    surveyQuestPage.isEmpty(), surveyQuestPage.getSort());
        }
    }
} 