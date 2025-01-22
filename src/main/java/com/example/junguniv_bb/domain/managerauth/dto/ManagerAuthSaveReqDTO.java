package com.example.junguniv_bb.domain.managerauth.dto;

import com.example.junguniv_bb.domain.managerauth.model.ManagerAuth;
import jakarta.validation.constraints.NotNull;

public record ManagerAuthSaveReqDTO(

        @NotNull(message = "메뉴 IDX는 필수입니다.")
        Long menuIdx, // 메뉴 IDX
        Long menuReadAuth, // 읽기 권한 레벨
        Long authLevel, // 레벨
        Long menuWriteAuth, // 등록 권한 레벨
        Long menuModifyAuth, // 수정 권한 레벨
        Long menuDeleteAuth // 삭제 권한 레벨
) {
    public ManagerAuth toEntity() {
        return ManagerAuth.builder()
            .menuIdx(menuIdx)
            .authLevel(authLevel)
            .menuReadAuth(menuReadAuth)
            .menuWriteAuth(menuWriteAuth)
            .menuModifyAuth(menuModifyAuth)
            .menuDeleteAuth(menuDeleteAuth)
            .build();
    }
}
