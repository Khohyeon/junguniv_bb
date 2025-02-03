package com.example.junguniv_bb.domain.subject.dto;

import com.example.junguniv_bb.domain.subject.model.Subject;

public record SubjectStep3SaveReqDTO(
        // step.3 운영 평가관리
        Long subjectIdx,
        Integer lcmsProgressFinishPercent,  // 수료기준 학습 진도율
        String lcmsProgressChkLimit,  // 순차학습 제한 여부 (Y/N)
        Integer lcmsLimitUnitPercent,  // 다음차시 학습가능 진도율 기준
        Integer lcmsProgress1dayPercent,  // 1일 최대 학습가능 기준 %
        Integer lcmsResultFinishPoint  // 수료기준 기준점수
) {
    public Subject step3Save() {
        return Subject.builder()
                .subjectIdx(subjectIdx)
                .lcmsProgressFinishPercent(lcmsProgressFinishPercent)
                .lcmsProgressChkLimit(lcmsProgressChkLimit)
                .lcmsLimitUnitPercent(lcmsLimitUnitPercent)
                .lcmsProgress1dayPercent(lcmsProgress1dayPercent)
                .lcmsResultFinishPoint(lcmsResultFinishPoint)
                .build();
    }
}
