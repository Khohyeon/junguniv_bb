package com.example.junguniv_bb.domain.managermenu.dto;

import com.example.junguniv_bb.domain.managermenu._enum.MenuType;

public record ManagerMenuSearchResDTO(
        Long menuIdx,
        String menuType,
        String menuName,
        String url,
        String chkUse
) {
}
