package com.example.junguniv_bb.domain.unzMenu.controller;

import com.example.junguniv_bb.dto.Depth3Menu;
import com.example.junguniv_bb.domain.unzMenu.model.UznMenu;
import com.example.junguniv_bb.domain.unzMenu.model.UznMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu")
public class UznMenuRestController {

    private final UznMenuRepository uznMenuRepository;

    @GetMapping("/depth3")
    public List<Depth3Menu> getDepth3Menus(@RequestParam Integer parentMenuIdx) {
        List<UznMenu> childMenus = uznMenuRepository.findByParent_MenuIdxAndChkUseOrderBySortno(parentMenuIdx, "Y");

        return childMenus.stream()
                .map(menu -> new Depth3Menu(
                        menu.getMenuName(),
                        menu.getUrl() != null ? menu.getUrl() : "#",
                        "_self",
                        true // 기본적으로 비활성화
                ))
                .toList();
    }
}
