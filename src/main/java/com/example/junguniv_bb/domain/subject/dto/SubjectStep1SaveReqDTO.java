package com.example.junguniv_bb.domain.subject.dto;

import com.example.junguniv_bb.domain.college.model.College;
import com.example.junguniv_bb.domain.major.model.Major;
import com.example.junguniv_bb.domain.subject.model.Subject;

public record SubjectStep1SaveReqDTO(
        // step.1 기본정보설정
        String subjectName,            // 과정명
        String subjectCode,            // 과정코드
        Long collegeIdx,               // 분야 Idx
        Long majorIdx,                 // 분류 Idx
        Long courseCapacity,           // 수강정원
        Long coursePrices,             // 수강료
        String coursetakeTarget,       // 수강대상
//        String fname1,                 // 썸네일 파일명
//        String fname1Name,             // 썸네일 공개명
        String subjectTegnames,        // 태그 키워드
        String progressInfo,           // 일일 학습량 안내
        String finishCriteriaGuide,    // 수료기준 안내
        String classAttendancetype,    // 출석 인증 방식
        String learningLevel,          // 학습 수준
        String learningGigan           // 학습기간
) {
    public Subject saveEntity(College college, Major major) {
        return Subject.builder()
                .subjectName(subjectName)
                .subjectCode(subjectCode)
                .college(college)
                .major(major)
                .courseCapacity(courseCapacity)
                .coursePrices(coursePrices)
                .coursetakeTarget(coursetakeTarget)
//                .fname1(fname1)
//                .fname1Name(fname1Name)
                .subjectTegnames(subjectTegnames)
                .progressInfo(progressInfo)
                .finishCriteriaGuide(finishCriteriaGuide)
                .classAttendancetype(classAttendancetype)
                .learningLevel(learningLevel)
                .learningGigan(learningGigan)
                .build();
    }
}
