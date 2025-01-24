package com.example.junguniv_bb.domain.managermenu.dto;

import com.example.junguniv_bb.domain.managermenu._enum.MenuType;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;

import java.util.List;

public record ManagerMenuDetailResDTO(
    // 부모 메뉴 정보 (간단히)
    ParentMenuInfo parent,
    // 메뉴 기본 정보
    ManagerMenuDTO managerMenu,
    // 자식 메뉴 정보 (간단히)
    List<ChildMenuInfo> children
) {
    // 메뉴 기본 정보를 담는 record
    public record ManagerMenuDTO(
        Long menuIdx,
        String menuName,
        Long sortno,
        String chkUse,
        String url,
        MenuType menuGroup,
        String menuHelp,
        String chkPerson,
        String personInfor
    ) {
        public static ManagerMenuDTO from(ManagerMenu menu) {
            return new ManagerMenuDTO(
                menu.getMenuIdx(),
                menu.getMenuName(),
                menu.getSortno(),
                menu.getChkUse(),
                menu.getUrl(),
                menu.getMenuGroup(),
                menu.getMenuHelp(),
                menu.getChkPerson(),
                menu.getPersonInfor()
            );
        }
    }

    // 부모 메뉴 정보를 담는 record
    public record ParentMenuInfo(
        Long menuIdx,
        String menuName,
        String url
    ) {
        public static ParentMenuInfo from(ManagerMenu parent) {
            if (parent == null) return null;
            return new ParentMenuInfo(
                parent.getMenuIdx(),
                parent.getMenuName(),
                parent.getUrl()
            );
        }
    }

    // 자식 메뉴 정보를 담는 record
    public record ChildMenuInfo(
        Long menuIdx,
        String menuName,
        Long sortno,
        String url,
        String chkUse
    ) {
        public static ChildMenuInfo from(ManagerMenu child) {
            return new ChildMenuInfo(
                child.getMenuIdx(),
                child.getMenuName(),
                child.getSortno(),
                child.getUrl(),
                child.getChkUse()
            );
        }
    }

    public static ManagerMenuDetailResDTO from(ManagerMenu menu) {
        return new ManagerMenuDetailResDTO(
            ParentMenuInfo.from(menu.getParent()),
            ManagerMenuDTO.from(menu),
            menu.getChildren().stream()
                .map(ChildMenuInfo::from)
                .toList()
        );
    }
}
