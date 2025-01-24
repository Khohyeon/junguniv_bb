package com.example.junguniv_bb.domain.managermenu._branch.controller;

import com.example.junguniv_bb.domain.managermenu._branch.dto.BranchMenuListResDTO;
import com.example.junguniv_bb.domain.managermenu._branch.dto.Depth3CountDTO;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;
import com.example.junguniv_bb.domain.managermenu.service.ManagerMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/masterpage_sys/branch")
@RequiredArgsConstructor
public class ManagerMenu_BranchController {

    private final ManagerMenuService managerMenuService;

    @GetMapping("/admin")
    public String branchMenuAdmin(@RequestParam(value = "menuGroup", required = false, defaultValue = "SYSTEM") String menuGroup, Model model) {

        // menuType에 따른 메뉴 필터링
        List<ManagerMenu> depth1Menus = managerMenuService.findMenusByLevelAndType(1L, menuGroup);
        List<ManagerMenu> depth2Menus = managerMenuService.findMenusByLevelAndType(2L, menuGroup);
        List<ManagerMenu> depth3Menus = managerMenuService.findMenusByLevelAndType(3L, menuGroup);

        // 3차 메뉴 개수 매핑
        Map<ManagerMenu, Long> depth3Counts = depth3Menus.stream()
                .collect(Collectors.groupingBy(
                        ManagerMenu::getParent, // PARENT_IDX 기준으로 그룹화
                        Collectors.counting()  // 각 그룹의 개수 계산
                ));

        // BranchMenuListResDTO 생성
        BranchMenuListResDTO branchMenuListResDTO = new BranchMenuListResDTO(depth1Menus, depth2Menus, depth3Menus, depth3Counts);

        model.addAttribute("branchMenuList", branchMenuListResDTO);
        model.addAttribute("currentMenuGroup", menuGroup); // 현재 선택된 메뉴 그룹

        return "masterpage_sys/branch/listForm";
    }

    @GetMapping("/teacher")
    public String branchMenuTeacher() {
        return "masterpage_sys/teacher/listForm";
    }

    @GetMapping("/company")
    public String branchMenuCompany() {
        return "masterpage_sys/company/listForm";
    }
}
