package com.example.junguniv_bb.domain.managerauth.dto;


import com.example.junguniv_bb.domain.managerauth.model.ManagerAuth;

public record ManagerAuthUpdateReqDTO(
        Long menuIdx,        // 메뉴 IDX
        Long authLevel,      // 레벨
        Long menuReadAuth,   // 읽기 권한 레벨
        Long menuWriteAuth,  // 등록 권한 레벨
        Long menuModifyAuth, // 수정 권한 레벨
        Long menuDeleteAuth  // 삭제 권한 레벨
) {
    public void updateEntity(ManagerAuth managerAuth) {
        // 메뉴에 대한 권한이 바뀌는 것이지 메뉴 idx는 바뀌지 않는다.
        if (authLevel != null) {
            managerAuth.setAuthLevel(authLevel);
        }
        if (menuReadAuth != null) {
            managerAuth.setMenuReadAuth(menuReadAuth);
        }
        if (menuWriteAuth != null) {
            managerAuth.setMenuWriteAuth(menuWriteAuth);
        }
        if (menuModifyAuth != null) {
            managerAuth.setMenuModifyAuth(menuModifyAuth);
        }
        if (menuDeleteAuth != null) {
            managerAuth.setMenuDeleteAuth(menuDeleteAuth);
        }
    }

    // 엔티티로 변환 메서드 Upsert (새로운 엔티티 생성 시 사용)
    public ManagerAuth toEntity() {
        return new ManagerAuth(
                null,          // authIdx는 자동 생성되므로 null로 설정
                menuIdx,
                authLevel,
                menuReadAuth,
                menuWriteAuth,
                menuModifyAuth,
                menuDeleteAuth
        );
    }
}

