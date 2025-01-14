package com.example.junguniv_bb.dto;

import java.util.List;

public record UznMenuListResDTO(
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
