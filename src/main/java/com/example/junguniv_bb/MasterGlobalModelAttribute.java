package com.example.junguniv_bb;

import com.example.junguniv_bb.domain.managermenu._enum.MenuType;
import com.example.junguniv_bb.domain.managermenu.dto.ManagerMenuListResDTO;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenuRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
@RequiredArgsConstructor
public class MasterGlobalModelAttribute {
    private final ManagerMenuRepository managerMenuRepository;


    /**
     * 시스템 설정 탭의 Header에 고정으로 넣는 데이터 값
     * 최상위 메뉴와 하위 메뉴를 포함한 메뉴 계층을 반환합니다.
     * @param request HTTP 요청 객체
     * @return 필터링된 메뉴 리스트
     */
    @ModelAttribute("xbigMenus")
    public List<ManagerMenuListResDTO> populateBigMenus(HttpServletRequest request) {
        // URL에서 MenuType 추출
        MenuType menuType = extractMenuTypeFromRequest(request);

        // 최상위 메뉴 조회 (MenuType에 따라 필터링)
        List<ManagerMenu> topMenus = managerMenuRepository.findByParentIsNullAndChkUseAndMenuGroupOrderBySortno("Y", menuType);

        // 메뉴 데이터를 변환하여 반환
        return topMenus.stream()
                .map(this::convertToMenu)
                .toList();
    }

    /**
     * 요청 URL에서 MenuType을 추출합니다.
     * @param request HTTP 요청 객체
     * @return MenuType (없으면 기본값 SYSTEM)
     */
    private MenuType extractMenuTypeFromRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();

        if (uri.contains("masterpage_sys")) {
            return MenuType.SYSTEM;
        } else if (uri.contains("masterpage_pro")) {
            return MenuType.ADMIN_REFUND;
        }

        // 기본값 설정
        return MenuType.SYSTEM;
    }
    
    /**
     * ManagerMenu 엔티티를 Menu DTO로 변환합니다.
     */
    private ManagerMenuListResDTO convertToMenu(ManagerMenu menu) {
        // 하위 메뉴를 Menu.SubMenu 객체로 변환
        List<ManagerMenuListResDTO.SubMenu> subMenuList = Optional.ofNullable(menu.getChildren())
                .orElse(Collections.emptyList())
                .stream()
                .filter(child -> "Y".equals(child.getChkUse())) // 사용 가능한 하위 메뉴만 필터링
                .map(sub -> new ManagerMenuListResDTO.SubMenu(
                        sub.getMenuIdx(),
                        sub.getMenuName(),
                        Optional.ofNullable(sub.getUrl()).orElse("#") // URL이 없으면 기본값 #
                ))
                .toList();

        // 최상위 메뉴를 Menu 객체로 변환
        return new ManagerMenuListResDTO(
                menu.getMenuIdx(),
                menu.getMenuName(),
                Optional.ofNullable(menu.getUrl()).orElse("#"), // URL이 없으면 기본값 #
                "_self",
                subMenuList
        );
    }
}
