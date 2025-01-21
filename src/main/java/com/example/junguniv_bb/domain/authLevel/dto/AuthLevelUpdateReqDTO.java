package com.example.junguniv_bb.domain.authLevel.dto;


import com.example.junguniv_bb.domain.authLevel.model.AuthLevel;
import jakarta.validation.constraints.Size;

public record AuthLevelUpdateReqDTO(
        @Size(max = 200, message = "권한명은 최대 200자까지 입력할 수 있습니다.")
        String authLevelName,              // 권한 레벨명
        Long authLevel,                   // 권한 레벨
        String chkViewJumin               // 주민번호 출력 여부
) {
    public void updateEntity(AuthLevel authLevelEntity) {
        if (authLevelName != null) {
            authLevelEntity.setAuthLevelName(authLevelName);
        }
        if (authLevel != null) {
            authLevelEntity.setAuthLevel(authLevel);
        }
        if (chkViewJumin != null) {
            authLevelEntity.setChkViewJumin(chkViewJumin);
        }
    }
}

