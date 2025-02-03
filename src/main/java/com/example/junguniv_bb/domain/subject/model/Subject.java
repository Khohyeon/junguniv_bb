package com.example.junguniv_bb.domain.subject.model;

import com.example.junguniv_bb._core.common.BaseTime;
import com.example.junguniv_bb.domain.college.model.College;
import com.example.junguniv_bb.domain.course.model.Course;
import com.example.junguniv_bb.domain.major.model.Major;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SUBJECT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUBJECT_IDX")
    private Long subjectIdx;  // 과목 IDX (기본키)

    // 🔹 ManyToOne 관계 설정 (FK)
    @ManyToOne
    @JoinColumn(name = "COLLEGE_IDX", nullable = false)
    private College college;  // 대학

    @ManyToOne
    @JoinColumn(name = "MAJOR_IDX", nullable = false)
    private Major major;  // 전공

//    @ManyToOne
//    @JoinColumn(name = "COURSE_IDX", nullable = false)
//    private Course course;  // 과정

    @Column(name = "SUBJECT_CODE", length = 100)
    private String subjectCode;  // 과정 코드

    @Column(name = "SUBJECT_NAME", length = 255)
    private String subjectName;  // 과정명

    @Column(name = "SUBJECT_TEGNAMES", length = 255)
    private String subjectTegnames;  // 태그 키워드

    @Column(name = "INTRODUCE", columnDefinition = "MEDIUMTEXT")
    private String introduce;  // 과정소개 (에디터)

    @Column(name = "EVALUATION_STATE_IS")
    private String evaluationStateIs;  // 평가적합여부

    @Column(name = "RESULT_PERFECT_LIMIT")
    private Integer resultPerfectLimit;  // 만점 기준

    @Column(name = "LCMS_RESULT_FINISH_POINT")
    private Integer lcmsResultFinishPoint;  // 수료기준 기준점수

    @Column(name = "LEARNING_LEVEL", length = 50)
    private String learningLevel;  // 학습 수준 (1=기초,2=하,3=중,4=상)

    @Column(name = "LEARNING_GIGAN", length = 50)
    private String learningGigan;  // 학습기간

    @Column(name = "CLASS_STUDYWAY", columnDefinition = "MEDIUMTEXT")
    private String classStudyway;  // 수업방법 (에디터)

    @Column(name = "CLASS_ATTENDANCETYPE", columnDefinition = "MEDIUMTEXT")
    private String classAttendancetype;  // 출석 인증 방식

    @Column(name = "PURPOSE", columnDefinition = "MEDIUMTEXT")
    private String purpose;  // 학습목표 (에디터)

    @Column(name = "BOOKS", columnDefinition = "MEDIUMTEXT")
    private String books;  // 교재 및 참고문헌

    @Column(name = "FINISH_CRITERIA_GUIDE", columnDefinition = "MEDIUMTEXT")
    private String finishCriteriaGuide;  // 수료기준 안내

    @Column(name = "LCMS_PROGRESS_1DAY_PERCENT")
    private Integer lcmsProgress1dayPercent;  // 1일 최대 학습가능 기준 %

    @Column(name = "LCMS_PROGRESS_FINISH_PERCENT")
    private Integer lcmsProgressFinishPercent;  // 수료기준 학습 진도율

    @Column(name = "LCMS_PROGRESS_CHK_LIMIT", length = 1)
    private String lcmsProgressChkLimit;  // 차시학습 진도 제한 (Y/N)

    @Column(name = "LCMS_LIMIT_UNIT_PERCENT")
    private Integer lcmsLimitUnitPercent;  // 단원 수업 최소학습율

    @Column(name = "LCMS_TIMES_NUMBERS")
    private Integer lcmsTimesNumbers;  // 차시 개수

    @Column(name = "CONTENTS_DEVELOPMENT_TYPE", length = 200)
    private String contentsDevelopmentType;  // 컨텐츠 개발 방식

    @Column(name = "REGIST_YEAR", length = 8)
    private String registYear;  // 심사차수 년도

    @Column(name = "REGIST_MONTH", length = 8)
    private String registMonth;  // 심사차수 월

    @Column(name = "REGIST_TIMES", length = 3)
    private String registTimes;  // 심사차수 일

    @Column(name = "FNAME1", length = 200)
    private String fname1;  // 썸네일 파일명

    @Column(name = "FNAME1_NAME", length = 200)
    private String fname1Name;  // 썸네일 공개명

    @Column(name = "STUDY_START_DATE", length = 10)
    private String studyStartDate;  // 교육기간 시작일

    @Column(name = "STUDY_END_DATE", length = 10)
    private String studyEndDate;  // 교육기간 종료일

    @Column(name = "COURSE_PRICES")
    private Long coursePrices;  // 수강료

    @Column(name = "COURSE_CAPACITY")
    private Long courseCapacity;  // 수강 정원

    @Column(name = "COURSETAKE_TARGET", length = 100)
    private String coursetakeTarget;  // 수강 대상

    @Column(name = "PROGRESS_INFO", length = 255)
    private String progressInfo;  // 일일 학습량 안내

    @Column(name = "SIMSA_STATE", length = 10)
    private String simsaState;  // 신청 상태

    @Column(name = "SIMSA_CODE", length = 10)
    private String simsaCode;  // e-simsa 과정 신청 코드

    @Column(name = "EXPIRY_DATE", length = 100)
    private String expiryDate;  //  e-simsa 유효기간

    @Column(name = "TRACSE_STATE")
    private String tracseState; // hrd-net 인정 체크

    @Column(name = "TRACSE_ID", length = 10)
    private String tracseId;  // hrd-net 과정 신청 코드

    @Column(name = "TRACSE_DATE", length = 100)
    private String tracseDate;  // hrd-net 유효기간
}
