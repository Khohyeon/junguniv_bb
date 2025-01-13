package com.example.junguniv_bb.masterpage.sys.dto;

import java.util.List;

public record Menu(
        String code,
        String name,
        String url,
        String target,
        List<SubMenu> subMenus
) {
    public record SubMenu(
            String code,
            String name,
            String url
    ) {
    }
}
