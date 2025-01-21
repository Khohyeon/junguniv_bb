package com.example.junguniv_bb._core.permission;

import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;
import org.springframework.security.access.ConfigAttribute;

public class MenuConfigAttribute implements ConfigAttribute {

    private final ManagerMenu managerMenu;

    public MenuConfigAttribute(ManagerMenu managerMenu) {
        this.managerMenu = managerMenu;
    }

    /**
     * 권한 유형을 반환
     */
    @Override
    public String getAttribute() {
        return "MENU_ACCESS"; // 메뉴 접근 권한
    }

    /**
     * ManagerMenu 엔티티 참조하여 권한 검증 시 필요한 정보를 제공
     */
    public ManagerMenu getManagerMenu() {
        return managerMenu;
    }
}


