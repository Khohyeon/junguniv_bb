package com.example.junguniv_bb.domain.survey._quest._item.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import com.example.junguniv_bb._core.common.BaseTime;

@Entity
@Table(name = "SURVEY_QUEST_ITEM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyQuestItem extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUEST_ITEM_IDX")
    private Long questItemIdx; // 문항 IDX
    
    @Column(name = "ITEM_CONTENT", columnDefinition = "mediumtext")
    private String itemContent; // 세부항목 문항내용
    
    @Column(name = "RULE_POINT")
    private Float rulePoint; // 배점
    
    @Column(name = "QUEST_IDX")
    private Long questIdx; // 질문IDX
    
    @Column(name = "SORTNO")
    private Integer sortNo; // 정렬값
    
    @Column(name = "RULE_ETC", length = 255)
    private String ruleEtc; // 추가설명
}
