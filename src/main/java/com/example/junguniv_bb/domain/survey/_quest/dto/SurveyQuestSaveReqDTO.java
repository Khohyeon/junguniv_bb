package com.example.junguniv_bb.domain.survey._quest.dto;

import com.example.junguniv_bb.domain.survey._quest.model.SurveyQuest;
import jakarta.validation.constraints.Size;

public record SurveyQuestSaveReqDTO(
    @Size(max = 100, message = "평가유형은 100자를 초과할 수 없습니다")
    String questType,     // 평가유형

    String questContent,  // 질문내용 (mediumtext)

    Long surveyIdx,       // 강의(설문)평가 IDX

    Long sortNo,          // 정렬값

    @Size(max = 1, message = "필수여부는 1자를 초과할 수 없습니다")
    String questRequired, // 필수여부

    @Size(max = 50, message = "평가유형은 50자를 초과할 수 없습니다")
    String questSubType   // 평가유형(단일, 다중선택)
) {
    public SurveyQuest toEntity() {
        return SurveyQuest.builder()
            .questType(questType)
            .questContent(questContent)
            .surveyIdx(surveyIdx)
            .sortNo(sortNo)
            .questRequired(questRequired)
            .questSubType(questSubType)
            .build();
    }
} 