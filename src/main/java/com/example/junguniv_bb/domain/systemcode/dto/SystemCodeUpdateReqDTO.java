package com.example.junguniv_bb.domain.systemcode.dto;

import com.example.junguniv_bb.domain.systemcode.model.SystemCode;
import jakarta.validation.constraints.Size;

public record SystemCodeUpdateReqDTO(
        @Size(max = 255, message = "시스템 코드 명은 최대 255자까지 입력할 수 있습니다.")
        String systemCodeName, // 시스템 코드 명
        @Size(max = 255, message = "시스템 코드 규칙은 최대 255자까지 입력할 수 있습니다.")
        String systemCodeRule, // 시스템 코드 규칙
        @Size(max = 200, message = "시스템 코드 그룹은 최대 200자까지 입력할 수 있습니다.")
        String systemCodeGroup, // 시스템 코드 그룹
        String systemCodeKey, // 시스템 코드 키
        String systemCodeValue // 시스템 코드 값
) {

    public void updateEntity(SystemCode systemCode) {

        if(systemCodeName != null) {
            systemCode.setSystemCodeName(systemCodeName);
        }

        if(systemCodeRule != null) {
            systemCode.setSystemCodeRule(systemCodeRule);
        }

        if(systemCodeGroup != null) {
            systemCode.setSystemCodeGroup(systemCodeGroup);
        }

        if(systemCodeKey != null) {
            systemCode.setSystemCodeKey(systemCodeKey);
        }

        if(systemCodeValue != null) {
            systemCode.setSystemCodeValue(systemCodeValue);
        }
    }
}
