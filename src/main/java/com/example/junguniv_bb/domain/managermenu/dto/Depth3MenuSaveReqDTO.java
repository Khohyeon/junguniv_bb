package com.example.junguniv_bb.domain.managermenu.dto;

import com.example.junguniv_bb.domain.managermenu._enum.MenuType;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;

public record Depth3MenuSaveReqDTO(
        String menuName,
        String menuGroup,
        Long parentIdx
) {

    public ManagerMenu saveEntity(Long maxSort, ManagerMenu managerMenu) {
        return new ManagerMenu(
                null, menuName, maxSort, "N", null, MenuType.valueOf(menuGroup), null,
                null, null, managerMenu, null, 3L
        );
    }
}