package com.example.junguniv_bb.domain.managermenu._branch.dto;

import com.example.junguniv_bb.domain.managermenu._enum.MenuType;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;

public record Depth1MenuSaveReqDTO(
        String menuName,
        String menuGroup
) {

    public ManagerMenu saveEntity(Long maxSort) {
        return new ManagerMenu(
                null, menuName, maxSort, "Y", null, MenuType.valueOf(menuGroup), null,
                null, null, null, null, 1L
        );
    }
}
