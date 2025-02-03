package com.example.junguniv_bb.domain.survey.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.example.junguniv_bb._core.common.BaseTime;

/**
 * 강의(설문)평가 엔티티
 */
@Entity
@Table(name = "SURVEY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Survey extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SURVEY_IDX")
    private Long surveyIdx; // 강의(설문)평가 IDX

    @Column(name = "SURVEY_TITLE", length = 255)
    private String surveyTitle; // 강의(설문)평가 제목1

    @Column(name = "SURVEY_TARGET", columnDefinition = "mediumtext")
    private String surveyTarget; // 강의(설문)평가 대상

    @Column(name = "CHK_OPEN", length = 1)
    private String chkOpen; // 공개 여부

    @Column(name = "SUBJECT_IDX")
    private Long subjectIdx; // 과정 IDX

    @Column(name = "SUBJECT_NAME", length = 255)
    private String subjectName; // 과정명

    @Column(name = "CHK_NAMES", length = 10)
    private String chkNames; // 의무차시

    @Column(name = "CHK_REQUIRE", length = 1)
    private String chkRequire; // 필수체크

    @Column(name = "SURVEY_VIEW_POINT", length = 50)
    private String surveyViewPoint; // 실시지점

}
