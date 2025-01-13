package com.example.junguniv_bb.masterpage.sys.dto;

import java.util.List;

public record Menu(
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
