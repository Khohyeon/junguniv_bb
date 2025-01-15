package com.example.junguniv_bb;

import com.example.junguniv_bb.dto.UznMenuListResDTO;
import com.example.junguniv_bb.domain.uznMenu.model.UznMenu;
import com.example.junguniv_bb.domain.uznMenu.model.UznMenuRepository;
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
    public List<UznMenuListResDTO> populateBigMenus() {
        // 최상위 메뉴 조회
        List<UznMenu> topMenus = uznMenuRepository.findByParentIsNullAndChkUseOrderBySortno("Y");

        // 메뉴 데이터를 변환하여 반환
        return topMenus.stream()
                .map(this::convertToMenu)
                .toList();
    }
    
    /**
     * UznMenu 엔티티를 Menu DTO로 변환합니다.
     */
    private UznMenuListResDTO convertToMenu(UznMenu menu) {
        // 하위 메뉴를 Menu.SubMenu 객체로 변환
        List<UznMenuListResDTO.SubMenu> subMenuList = Optional.ofNullable(menu.getChildren())
                .orElse(Collections.emptyList())
                .stream()
                .filter(child -> "Y".equals(child.getChkUse())) // 사용 가능한 하위 메뉴만 필터링
                .map(sub -> new UznMenuListResDTO.SubMenu(
                        sub.getMenuIdx(),
                        sub.getMenuName(),
                        Optional.ofNullable(sub.getUrl()).orElse("#") // URL이 없으면 기본값 #
                ))
                .toList();

        // 최상위 메뉴를 Menu 객체로 변환
        return new UznMenuListResDTO(
                menu.getMenuIdx(),
                menu.getMenuName(),
                Optional.ofNullable(menu.getUrl()).orElse("#"), // URL이 없으면 기본값 #
                "_self",
                subMenuList
        );
    }
}
