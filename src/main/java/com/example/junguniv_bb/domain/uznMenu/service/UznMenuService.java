package com.example.junguniv_bb.domain.uznMenu.service;


import com.example.junguniv_bb.dto.UznDepth3MenuListResDTO;
import com.example.junguniv_bb.dto.UznMenuListResDTO;
import com.example.junguniv_bb.domain.uznMenu.model.UznMenu;
import com.example.junguniv_bb.domain.uznMenu.model.UznMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UznMenuService {

    private final UznMenuRepository uznMenuRepository;

    public List<UznDepth3MenuListResDTO> getDepth3Menus(Integer parentMenuIdx) {
        List<UznMenu> childMenus = uznMenuRepository.findByParent_MenuIdxAndChkUseOrderBySortno(parentMenuIdx, "Y");

        return childMenus.stream()
                .map(menu -> new UznDepth3MenuListResDTO(
                        menu.getMenuName(),
                        menu.getUrl() != null ? menu.getUrl() : "#",
                        "_self",
                        true // 기본적으로 비활성화
                ))
                .toList();
    }
}
