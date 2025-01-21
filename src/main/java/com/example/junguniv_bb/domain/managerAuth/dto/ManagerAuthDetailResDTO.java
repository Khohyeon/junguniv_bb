package com.example.junguniv_bb.domain.managerAuth.dto;


import java.time.LocalDateTime;

import com.example.junguniv_bb.domain.managerAuth.model.ManagerAuth;

public record ManagerAuthDetailResDTO(
    Long authIdx,
    Long menuIdx,
    Long authLevel,
    Long menuReadAuth,
    Long menuWriteAuth,
    Long menuModifyAuth,
    Long menuDeleteAuth
) {
    public static ManagerAuthDetailResDTO from(ManagerAuth managerAuth) {
        return new ManagerAuthDetailResDTO(
            managerAuth.getAuthIdx(),
            managerAuth.getMenuIdx(),
            managerAuth.getAuthLevel(),
            managerAuth.getMenuReadAuth(),
            managerAuth.getMenuWriteAuth(),
            managerAuth.getMenuModifyAuth(),
            managerAuth.getMenuDeleteAuth()
        );
    }
}
