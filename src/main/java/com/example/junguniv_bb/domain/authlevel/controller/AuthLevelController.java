package com.example.junguniv_bb.domain.authlevel.controller;

import com.example.junguniv_bb._core.security.CustomUserDetails;
import com.example.junguniv_bb.domain.authlevel.dto.AuthLevelDetailResDTO;
import com.example.junguniv_bb.domain.authlevel.service.AuthLevelService;
import com.example.junguniv_bb.domain.managerauth.model.ManagerAuth;
import com.example.junguniv_bb.domain.managerauth.service.ManagerAuthService;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;
import com.example.junguniv_bb.domain.managermenu.service.ManagerMenuService;
import com.example.junguniv_bb.domain.managermenu.dto.ManagerMenuDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/auth_level")
@Slf4j
public class AuthLevelController {

    /* DI */
    private final AuthLevelService authLevelService;
    private final ManagerMenuService managerMenuService;
    private final ManagerAuthService managerAuthService;
    private final ObjectMapper objectMapper;


    /* 관리자권한 목록 페이지 */
    @GetMapping
    public String authLevelListForm(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 10) Pageable pageable, @RequestParam(required = false) String authLevelName) {

        return "masterpage_sys/auth_level/listForm";
    }

    /* 관리자권한 상세보기 페이지 */
    @GetMapping("/{id}")
    public String authLevelDetailForm(Model model, @PathVariable Long id) throws JsonProcessingException {
        // DB 조회
        AuthLevelDetailResDTO resDTO = authLevelService.authLevelDetail(id); // AuthLevel 상세 정보 가져오기
        List<ManagerMenu> menuList = managerMenuService.findAll(); // 모든 메뉴 가져오기
        Long authLevelValue = resDTO.authLevel(); // AuthLevel의 authLevel 값 가져오기
        List<ManagerAuth> managerAuthList = managerAuthService.findAllByAuthLevel(authLevelValue); // 해당 AuthLevel의 ManagerAuth 가져오기

        // 중복 키 처리
        Map<Long, ManagerAuth> managerAuthMap = managerAuthList.stream()
                .collect(Collectors.toMap(
                    ManagerAuth::getMenuIdx,
                    Function.identity(),
                    (existing, replacement) -> {
                        log.warn("중복된 menuIdx 발견: {}", existing.getMenuIdx());
                        return existing;
                    }
                ));

        // 메뉴 목록을 DTO로 변환
        List<ManagerMenuDTO> managerMenuDTOList = menuList.stream()
                .map(menu -> new ManagerMenuDTO(
                    menu.getMenuIdx(),
                    menu.getMenuName(),
                    menu.getMenuGroup() != null ? menu.getMenuGroup().toString() : null,
                    menu.getParent() != null ? menu.getParent().getMenuIdx() : null
                ))
                .collect(Collectors.toList());

        // 모델에 추가
        model.addAttribute("authLevelDetail", resDTO);
        model.addAttribute("menuList", managerMenuDTOList);
        model.addAttribute("menuListAsJson", objectMapper.writeValueAsString(managerMenuDTOList));
        model.addAttribute("managerAuthMap", managerAuthMap);

        return "masterpage_sys/auth_level/detailForm";
    }

    /* 관리자 권한 등록 페이지 */
    @GetMapping("/save")
    public String authLevelSaveForm(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws JsonProcessingException {
        // 메뉴 목록 조회
        List<ManagerMenu> menuList = managerMenuService.findAll();
        
        // 메뉴 목록을 DTO로 변환
        List<ManagerMenuDTO> managerMenuDTOList = menuList.stream()
                .map(menu -> new ManagerMenuDTO(
                    menu.getMenuIdx(),
                    menu.getMenuName(),
                    menu.getMenuGroup() != null ? menu.getMenuGroup().toString() : null,
                    menu.getParent() != null ? menu.getParent().getMenuIdx() : null
                ))
                .collect(Collectors.toList());

        // 모델에 추가
        model.addAttribute("menuList", managerMenuDTOList);
        model.addAttribute("menuListAsJson", objectMapper.writeValueAsString(managerMenuDTOList));
        
        return "masterpage_sys/auth_level/saveForm";
    }

}
