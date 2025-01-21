package com.example.junguniv_bb.domain.managerMenu.service;


import com.example.junguniv_bb.domain.managerMenu.dto.ManagerMenuDepth3ListResDTO;
import com.example.junguniv_bb.domain.managerMenu.model.ManagerMenu;
import com.example.junguniv_bb.domain.managerMenu.model.ManagerMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerMenuService {

    private final ManagerMenuRepository managerMenuRepository;

    public List<ManagerMenuDepth3ListResDTO> getDepth3Menus(Integer parentMenuIdx) {
        List<ManagerMenu> childMenus = managerMenuRepository.findByParent_MenuIdxAndChkUseOrderBySortno(parentMenuIdx, "Y");

        return childMenus.stream()
                .map(menu -> new ManagerMenuDepth3ListResDTO(
                        menu.getMenuName(),
                        menu.getUrl() != null ? menu.getUrl() : "#",
                        "_self",
                        true // 기본적으로 비활성화
                ))
                .toList();
    }
}
