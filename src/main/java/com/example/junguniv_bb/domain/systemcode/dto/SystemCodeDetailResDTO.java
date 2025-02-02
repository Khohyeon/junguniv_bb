package com.example.junguniv_bb.domain.systemcode.dto;

import com.example.junguniv_bb.domain.systemcode.model.SystemCode;

public record SystemCodeDetailResDTO(
    Long systemCodeIdx, // 시스템 코드 IDX
    String systemCodeName, // 시스템 코드 이름
    String systemCodeRule, // 시스템 코드 규칙
    String systemCodeGroup, // 시스템 코드 그룹
    String systemCodeKey, // 시스템 코드 키
    String systemCodeValue // 시스템 코드 값
) {
    public static SystemCodeDetailResDTO from(SystemCode systemCode) {
        return new SystemCodeDetailResDTO(
            systemCode.getSystemCodeIdx(),
            systemCode.getSystemCodeName(),
            systemCode.getSystemCodeRule(),
            systemCode.getSystemCodeGroup(),
            systemCode.getSystemCodeKey(),
            systemCode.getSystemCodeValue()
        );
    }
}
