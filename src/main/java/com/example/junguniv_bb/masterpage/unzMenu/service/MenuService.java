package com.example.junguniv_bb.masterpage.unzMenu.service;


import com.example.junguniv_bb.masterpage.sys.dto.Menu;
import com.example.junguniv_bb.masterpage.unzMenu.model.UznMenu;
import com.example.junguniv_bb.masterpage.unzMenu.model.UznMenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final UznMenuRepository uznMenuRepository;

    public MenuService(UznMenuRepository uznMenuRepository) {
        this.uznMenuRepository = uznMenuRepository;
    }

    public void populateBigMenus(Model model) {
        // 최상위 메뉴 조회
        List<UznMenu> rootMenus = uznMenuRepository.findByParentIsNullAndChkUseOrderBySortno("Y");

        // 메뉴 계층을 DTO로 변환
        List<Menu> menus = rootMenus.stream()
                .map(this::convertToMenu)
                .collect(Collectors.toList());

        model.addAttribute("xbigMenus", menus);
    }

    // UznMenu -> Menu DTO 변환 메서드
    private Menu convertToMenu(UznMenu uznMenu) {
        List<Menu.SubMenu> subMenus = uznMenu.getChildren().stream()
                .map(this::convertToSubMenu)
                .collect(Collectors.toList());

        return new Menu(
                uznMenu.getMenuIdx(),
                uznMenu.getMenuName(),
                uznMenu.getUrl(),
                "_self",
                subMenus
        );
    }

    // UznMenu -> SubMenu DTO 변환 메서드
    private Menu.SubMenu convertToSubMenu(UznMenu uznMenu) {
        return new Menu.SubMenu(
                uznMenu.getMenuIdx(),
                uznMenu.getMenuName(),
                uznMenu.getUrl()
        );
    }
}
