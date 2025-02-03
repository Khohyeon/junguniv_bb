package com.example.junguniv_bb.domain.survey._quest.dto;

import com.example.junguniv_bb.domain.survey._quest.model.SurveyQuest;

public record SurveyQuestDetailResDTO(
    Long questIdx,        // 세부항목 IDX
    String questType,     // 평가유형
    String questContent,  // 질문내용
    Long surveyIdx,       // 강의(설문)평가 IDX
    Long sortNo,          // 정렬값
    String questRequired, // 필수여부
    String questSubType   // 평가유형(단일, 다중선택)
) {
    public static SurveyQuestDetailResDTO from(SurveyQuest surveyQuest) {
        return new SurveyQuestDetailResDTO(
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