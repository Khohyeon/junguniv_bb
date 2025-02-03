package com.example.junguniv_bb.domain.survey._quest._answer.model;

import jakarta.persistence.*;
import lombok.*;

import com.example.junguniv_bb._core.common.BaseTime;

@Entity
@Table(name = "SURVEY_QUEST_ANSWER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyQuestAnswer extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUEST_ANSWER_IDX")
    private Long questAnswerIdx; // 답변 IDX

    @Column(name = "SUBJ_ANSWER", columnDefinition = "mediumtext")
    private String subjAnswer; // 답변

    @Column(name = "SURVEY_IDX")
    private Long surveyIdx; // 강의(설문)평가 idx

    @Column(name = "QUEST_IDX")
    private Long questIdx; // 강의(설문)평가 세부항목idx

    @Column(name = "ANSWER_USER_ID", length = 100)
    private String answerUserId; // 답변회원 id

    @Column(name = "TARGET_USER_ID", length = 100)
    private String targetUserId; // 답변회원 id(모니터링 테이블에 있음)

    @Column(name = "CLIENT_IP", length = 100)
    private String clientIp; // 회원ip

    @Column(name = "MASTER_ID", length = 100)
    private String masterId; // 미사용

    @Column(name = "SUBJECT_IDX")
    private Long subjectIdx; // 과정idx

    @Column(name = "COURSE_IDX")
    private Long courseIdx; // 과정idx

    @Column(name = "OBJ_ANSWER_ITEM_ID")
    private Long objAnswerItemId; // 객관식답변

    @Column(name = "ANSWER_ETC", length = 255)
    private String answerEtc; // 추가질문답변

    @Column(name = "ITEM_IDS", length = 100)
    private String itemIds; // 객관식 다중선택시 저장되는 결과
}
