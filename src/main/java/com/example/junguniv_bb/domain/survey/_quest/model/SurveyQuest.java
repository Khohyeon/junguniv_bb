package com.example.junguniv_bb.domain.survey._quest.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.example.junguniv_bb._core.common.BaseTime;

@Entity
@Table(name = "SURVEY_QUEST")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SurveyQuest extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUEST_IDX")
    private Long questIdx; // 세부항목 IDX

    @Column(name = "QUEST_TYPE", length = 100)
    private String questType; // 평가유형

    @Column(name = "QUEST_CONTENT", columnDefinition = "mediumtext")
    private String questContent; // 질문내용

    @Column(name = "SURVEY_IDX")
    private Long surveyIdx; // 강의(설문)평가 IDX

    @Column(name = "SORTNO")
    private Long sortNo; // 정렬값

    @Column(name = "QUEST_REQUIRED", length = 1)
    private String questRequired; // 필수여부

    @Column(name = "QUEST_SUB_TYPE", length = 50)
    private String questSubType; // 평가유형(단일, 다중선택)
}
