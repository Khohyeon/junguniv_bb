package com.example.junguniv_bb.domain.managerMenu.dto;

import java.util.List;

public record ManagerMenuListResDTO(
        Long code,
        String name,
        String url,
        String target,
        List<SubMenu> subMenus
) {
    public record SubMenu(
            Long code,
            String name,
            String url
    ) {
    }
}
