package com.example.junguniv_bb.domain.managermenu._branch.dto;

import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;

import java.util.List;
import java.util.Map;

public record BranchMenuListResDTO(
        List<ManagerMenu> depth1Menus,
        List<ManagerMenu> depth2Menus,
        Map<ManagerMenu, Long> depth3Counts
) {
}
