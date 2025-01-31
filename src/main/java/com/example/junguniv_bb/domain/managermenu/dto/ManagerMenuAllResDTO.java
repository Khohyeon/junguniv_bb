package com.example.junguniv_bb.domain.managermenu.dto;

import com.example.junguniv_bb.domain.managermenu._enum.MenuType;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;

public record ManagerMenuAllResDTO(
    Long menuIdx,
    String menuName,
    Long sortno,
    String chkUse,
    String url,
    MenuType menuGroup,
    Long menuLevel,
    ParentMenuDTO parent
) {
    public record ParentMenuDTO(
        Long menuIdx,
        String menuName
    ) {}

    public static ManagerMenuAllResDTO from(ManagerMenu menu) {
        return new ManagerMenuAllResDTO(
            menu.getMenuIdx(),
            menu.getMenuName(),
            menu.getSortno(),
            menu.getChkUse(),
            menu.getUrl(),
            menu.getMenuGroup(),
            menu.getMenuLevel(),
            menu.getParent() != null 
                ? new ParentMenuDTO(menu.getParent().getMenuIdx(), menu.getParent().getMenuName()) 
                : null
        );
    }
} 