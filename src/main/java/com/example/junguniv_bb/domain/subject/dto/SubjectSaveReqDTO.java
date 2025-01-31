package com.example.junguniv_bb.domain.subject.dto;

public record SubjectSaveReqDTO(
        // step.1 기본정보설정
        String subjectName, // 과정명
        String subjectCode, // 과정코드
        Long collegeIdx, // 분야 Idx
        Long majorIdx,  //  분류 Idx
        Long courseCapacity, // 수강정원
        Long coursePrices, // 수강료
        String coursetakeTarget, // 수강대상
        String fname1,   // 썸네일 파일명
        String fname1Name,  // 썸네일 공개명
        String subjectTegnames, // 태그 키워드
        String progressInfo,  // 일일 학습량 안내
        String finishCriteriaGuide,  // 수료기준 안내
        String classAttendancetype,  // 출석 인증 방식
        String learningLevel,  // 학습 수준
        String learningGigan,  // 학습기간

        // step.2 심사 강사 정보
        String registYear,  // 심사차수 년도
        String registMonth,  // 심사차수 월
        String registTimes,  // 심사차수 일
        String simsaState,  // 신청 상태
        String evaluationStateIs,  // 평가적합여부
        String simsaCode,  // e-simsa 과정 신청 코드
        String expiryStartDate,  // e-simsa 유효기간
        String expiryEndDate,  // e-simsa 유효기간
        String tracseState,  // hrd-net 인정 체크
        String tracseId,  // hrd-net 과정 신청 코드
        String tracseStartDate,  // hrd-net 유효기간
        String tracseEndDate,  // hrd-net 유효기간

        // step.3 운영 평가관리
        Integer lcmsProgressFinishPercent,  // 수료기준 학습 진도율
        String lcmsProgressChkLimit,  // 순차학습 제한 여부 (Y/N)
        Integer lcmsLimitUnitPercent,  // 다음차시 학습가능 진도율 기준
        Integer lcmsProgress1dayPercent,  // 1일 최대 학습가능 기준 %
        Integer lcmsResultFinishPoint  // 수료기준 기준점수

        /**
         * TODO 추가 해야할 데이터
         * step1. 지원금
         * step2. 콘텐츠 및 강사
         * step3. 평가 구분/항목 및 평가방식 안내
         * step4. 교재 및 과정 선택
         */

) {
}
