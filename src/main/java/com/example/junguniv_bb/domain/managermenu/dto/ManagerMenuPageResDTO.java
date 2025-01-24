package com.example.junguniv_bb.domain.managermenu.dto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;

import java.util.List;

public record ManagerMenuPageResDTO(
    List<ManagerMenuDTO> managerMenuList,
    PageableDTO pageable
) {
    // 메뉴 목록용 간단 정보를 담는 record
    public record ManagerMenuDTO(
        Long menuIdx,
        String menuName,
        Long sortno,
        String chkUse,
        String url,
        // 계층 구조 파악을 위한 최소 정보
        ParentInfo parent,
        Long menuLevel,
        int childCount  // 자식 메뉴 개수
    ) {
        public record ParentInfo(
            Long menuIdx,
            String menuName
        ) {
            public static ParentInfo from(ManagerMenu parent) {
                if (parent == null) return null;
                return new ParentInfo(
                    parent.getMenuIdx(),
                    parent.getMenuName()
                );
            }
        }

        public static ManagerMenuDTO from(ManagerMenu menu) {
            return new ManagerMenuDTO(
                menu.getMenuIdx(),
                menu.getMenuName(),
                menu.getSortno(),
                menu.getChkUse(),
                menu.getUrl(),
                ParentInfo.from(menu.getParent()),
                menu.getMenuLevel(),
                menu.getChildren().size()  // 자식 메뉴 개수만 표시
            );
        }
    }

    public record PageableDTO(
        int pageNumber,
        int pageSize,
        int totalPages,
        long totalElements,
        boolean isLast,
        int numberOfElements,
        boolean isEmpty,
        Sort sort
    ) {
        public static PageableDTO from(Page<ManagerMenu> managerMenuPage) {
            return new PageableDTO(
                managerMenuPage.getNumber(),
                managerMenuPage.getSize(),
                managerMenuPage.getTotalPages(),
                managerMenuPage.getTotalElements(),
                managerMenuPage.isLast(),
                managerMenuPage.getNumberOfElements(),
                managerMenuPage.isEmpty(),
                managerMenuPage.getSort()
            );
        }
    }

    public static ManagerMenuPageResDTO from(Page<ManagerMenu> managerMenuPage) {
        List<ManagerMenuDTO> managerMenuList = managerMenuPage.getContent().stream()
            .map(ManagerMenuDTO::from)
            .toList();

        return new ManagerMenuPageResDTO(
            managerMenuList,
            PageableDTO.from(managerMenuPage)
        );
    }
}
