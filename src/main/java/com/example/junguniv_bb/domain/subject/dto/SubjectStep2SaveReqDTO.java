package com.example.junguniv_bb.domain.subject.dto;

import com.example.junguniv_bb.domain.subject.model.Subject;

public record SubjectStep2SaveReqDTO(
        // step.2 심사 강사 정보
        Long subjectIdx,
        String registYear,      // 심사차수 년도
        String registMonth,     // 심사차수 월
        String registTimes,     // 심사차수 일
        String simsaState,      // 신청 상태
        String evaluationStateIs,  // 평가적합여부
        String simsaCode,       // e-simsa 과정 신청 코드
        String expiryStartDate, // e-simsa 유효기간 시작
        String expiryEndDate,   // e-simsa 유효기간 종료
        String tracseState,     // hrd-net 인정 체크
        String tracseId,        // hrd-net 과정 신청 코드
        String tracseStartDate, // hrd-net 유효기간 시작
        String tracseEndDate    // hrd-net 유효기간 종료
) {
    public Subject step2Save() {
        // expiryPeriod와 tracsePeriod를 합쳐서 하나의 문자열로 생성
        String expiryPeriod = expiryStartDate + " ~ " + expiryEndDate;
        String tracsePeriod = tracseStartDate + " ~ " + tracseEndDate;
        return Subject.builder()
                .subjectIdx(subjectIdx)
                .registYear(registYear)
                .registMonth(registMonth)
                .registTimes(registTimes)
                .simsaState(simsaState)
                .evaluationStateIs(evaluationStateIs)
                .simsaCode(simsaCode)
                .expiryDate(expiryPeriod)      // 합친 날짜 범위 넣기
                .tracseState(tracseState)
                .tracseId(tracseId)
                .tracseDate(tracsePeriod)      // 합친 날짜 범위 넣기
                .build();
    }
}
