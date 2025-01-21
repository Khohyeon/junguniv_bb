package com.example.junguniv_bb.domain.managerMenu.controller;

import com.example.junguniv_bb.domain.managerMenu.service.ManagerMenuService;
import com.example.junguniv_bb.domain.managerMenu.dto.ManagerMenuDepth3ListResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/manager_menu/api")
public class ManagerMenuRestController {

    private final ManagerMenuService managerMenuService;

    /**
     * 2차 메뉴를 선택 했을 때 Rest API 요청
     */
    @GetMapping("/depth3")
    public List<ManagerMenuDepth3ListResDTO> getDepth3Menus(@RequestParam Integer parentMenuIdx) {
        return managerMenuService.getDepth3Menus(parentMenuIdx);
    }
}
