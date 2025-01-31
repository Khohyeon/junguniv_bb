package com.example.junguniv_bb.domain.course.model;

import com.example.junguniv_bb.domain.college.model.College;
import com.example.junguniv_bb.domain.major.model.Major;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "COURSE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COURSE_IDX")
    private Integer courseIdx;  // 과정 IDX (기본키)

    @ManyToOne
    @JoinColumn(name = "COLLEGE_IDX", nullable = false)
    private College college;  // FK - 대학 IDX

    @ManyToOne
    @JoinColumn(name = "MAJOR_IDX", nullable = false)
    private Major major;  // FK - 전공 IDX

    @Column(name = "COURSE_NAME", length = 255)
    private String courseName;  // 과정 이름

    @Column(name = "COURSE_CODE", length = 100)
    private String courseCode;  // 과정 코드

    @Column(name = "COURSE_TYPE", length = 100)
    private String courseType;  // 과정 타입

    @Column(name = "COURSE_PRICES")
    private Integer coursePrices;  // 과정 가격 (수강료)

    @Column(name = "UNIT_POINT")
    private Integer unitPoint;  // 학점

    @Column(name = "COURSE_PERIOD", length = 100)
    private String coursePeriod;  // 과정 기간

    @Column(name = "COURSE_CAPACITY")
    private Integer courseCapacity;  // 정원

    @Column(name = "OPEN_STATE", length = 100)
    private String openState;  // 공개 여부 (접수 상태)

    @Column(name = "NYEAR", length = 4)
    private String nyear;  // 년도

    @Column(name = "MONTH", length = 4)
    private String month;  // 월

    @Column(name = "CLASS_NAME", length = 200)
    private String className;  // 반 이름 (기수)

    @Column(name = "TRACSE_TME")
    private Integer tracseTme;  // 회차

    @Column(name = "SUBJECT_IDX")
    private Integer subjectIdx;  // 과목 IDX

    @Column(name = "RECEIPTPERIOD_START_DATE", length = 10)
    private String receiptPeriodStartDate;  // 접수기간 시작일

    @Column(name = "RECEIPTPERIOD_END_DATE", length = 10)
    private String receiptPeriodEndDate;  // 접수기간 마감일

    @Column(name = "STUDY_START_DATE", length = 10)
    private String studyStartDate;  // 교육기간 시작일

    @Column(name = "STUDY_END_DATE", length = 10)
    private String studyEndDate;  // 교육기간 종료일

    @Column(name = "CHK_COMPANY", length = 1)
    private String chkCompany;  // 특정기업 전용 강좌 여부 (Y/N)

    @Column(name = "CHK_MONITERING", length = 1)
    private String chkMonitering;  // 모니터링 사용 여부 (Y/N)

    @Column(name = "CHK_MOTP", length = 1)
    private String chkMotp;  // MOTP 사용 여부 (Y/N)

    @Column(name = "CHK_AI_NOTICE_USE", length = 1)
    private String chkAiNoticeUse;  // 평가 알림 여부 (Y/N)

    @Column(name = "CHK_BOK", length = 1)
    private String chkBok;  // 복습 기간 (Y/N)

    @Column(name = "CHK_STUDYLIMIT", length = 1)
    private String chkStudyLimit;  // 50% 학습 제한 (Y/N)

    @Column(name = "CHK_BOOKPRICE", length = 1)
    private String chkBookPrice;  // 유/무료 여부 (Y/N)

    @Column(name = "MINUS_PRICES", columnDefinition = "INT UNSIGNED")
    private Integer minusPrices;  // 할인가

    @Column(name = "PLUS_PRICES", columnDefinition = "INT UNSIGNED")
    private Integer plusPrices;  // 추가액

    @Column(name = "MINUS_TYPE", length = 100)
    private String minusType;  // 할인 타입

    @Column(name = "PLUS_TYPE", length = 100)
    private String plusType;  // 추가 금액 타입
}
