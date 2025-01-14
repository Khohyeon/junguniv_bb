package com.example.junguniv_bb.domain.uznMenu.controller;

import com.example.junguniv_bb.domain.uznMenu.service.UznMenuService;
import com.example.junguniv_bb.dto.UznDepth3MenuListResDTO;
import com.example.junguniv_bb.domain.uznMenu.model.UznMenu;
import com.example.junguniv_bb.domain.uznMenu.model.UznMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/uznMenu/api")
public class UznMenuRestController {

    private final UznMenuService uznMenuService;

    /**
     * 2차 메뉴를 선택 했을 때 Rest API 요청
     */
    @GetMapping("/depth3")
    public List<UznDepth3MenuListResDTO> getDepth3Menus(@RequestParam Integer parentMenuIdx) {
        return uznMenuService.getDepth3Menus(parentMenuIdx);
    }
}
