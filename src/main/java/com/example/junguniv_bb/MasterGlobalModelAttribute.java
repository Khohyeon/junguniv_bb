package com.example.junguniv_bb;

import com.example.junguniv_bb.dto.Menu;
import com.example.junguniv_bb.domain.unzMenu.model.UznMenu;
import com.example.junguniv_bb.domain.unzMenu.model.UznMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
@RequiredArgsConstructor
public class MasterGlobalModelAttribute {
    private final UznMenuRepository uznMenuRepository;


    /**
     * 시스템 설정 탭의 Header에 고정으로 넣는 데이터 값
     * 최상위 메뉴와 하위 메뉴를 포함한 메뉴 계층을 반환합니다.
     */
    @ModelAttribute("xbigMenus")
    public List<Menu> populateBigMenus() {
        // 최상위 메뉴 조회
        List<UznMenu> topMenus = uznMenuRepository.findByParentIsNullAndChkUseOrderBySortno("Y");

        // 메뉴 데이터를 변환하여 반환
        return topMenus.stream()
                .map(this::convertToMenu)
                .toList();
    }

//    /**
//     * 선택된 메뉴의 하위 탭 메뉴를 반환합니다.
//     */
//    @ModelAttribute("depth3Menus")
//    public List<Depth3Menu> populateDepth3Menus(@RequestParam(value = "menuIdx", required = false) Integer menuIdx) {
//        if (menuIdx == null) {
//            return List.of(); // 메뉴가 선택되지 않았다면 빈 리스트 반환
//        }
//
//        // 선택된 메뉴의 하위 메뉴(탭 메뉴) 조회
//        List<UznMenu> childMenus = uznMenuRepository.findAllByParentMenu_MenuIdxAndChkUseOrderBySortno(menuIdx, "Y");
//
//        // 하위 메뉴를 Depth3Menu DTO로 변환
//        return childMenus.stream()
//                .map(menu -> new Depth3Menu(
//                        menu.getMenuName(),             // 메뉴 이름
//                        menu.getUrl() != null ? menu.getUrl() : "#", // URL이 없으면 기본값 #
//                        "_self",                        // 기본 타겟
//                        false                           // 활성화 여부는 기본값 false
//                ))
//                .toList();
//    }
    /**
     * UznMenu 엔티티를 Menu DTO로 변환합니다.
     */
    private Menu convertToMenu(UznMenu menu) {
        // 하위 메뉴를 Menu.SubMenu 객체로 변환
        List<Menu.SubMenu> subMenuList = Optional.ofNullable(menu.getChildren())
                .orElse(Collections.emptyList())
                .stream()
                .filter(child -> "Y".equals(child.getChkUse())) // 사용 가능한 하위 메뉴만 필터링
                .map(sub -> new Menu.SubMenu(
                        sub.getMenuIdx(),
                        sub.getMenuName(),
                        Optional.ofNullable(sub.getUrl()).orElse("#") // URL이 없으면 기본값 #
                ))
                .toList();

        // 최상위 메뉴를 Menu 객체로 변환
        return new Menu(
                menu.getMenuIdx(),
                menu.getMenuName(),
                Optional.ofNullable(menu.getUrl()).orElse("#"), // URL이 없으면 기본값 #
                "_self",
                subMenuList
        );
    }
}
