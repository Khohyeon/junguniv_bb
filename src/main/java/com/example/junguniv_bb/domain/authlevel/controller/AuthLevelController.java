package com.example.junguniv_bb.domain.authlevel.controller;

import com.example.junguniv_bb._core.security.CustomUserDetails;
import com.example.junguniv_bb.domain.authlevel.dto.AuthLevelDetailResDTO;
import com.example.junguniv_bb.domain.authlevel.dto.AuthLevelPageResDTO;
import com.example.junguniv_bb.domain.authlevel.service.AuthLevelService;
import com.example.junguniv_bb.domain.managerauth.model.ManagerAuth;
import com.example.junguniv_bb.domain.managerauth.service.ManagerAuthService;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;
import com.example.junguniv_bb.domain.managermenu.service.ManagerMenuService;
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
    public String listForm(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 10) Pageable pageable, @RequestParam(required = false) String authLevelName) {

        return "masterpage_sys/auth_level/listForm";
    }

    /* 관리자권한 상세보기 페이지 */
    @GetMapping("/{id}")
    public String detailForm(Model model, @PathVariable Long id) throws JsonProcessingException {
        // DB 조회
        AuthLevelDetailResDTO resDTO = authLevelService.authLevelDetail(id); // AuthLevel 상세 정보 가져오기
        List<ManagerMenu> menuList = managerMenuService.findAll(); // 모든 메뉴 가져오기
        Long authLevelValue = resDTO.authLevel(); // AuthLevel의 authLevel 값 가져오기
        List<ManagerAuth> managerAuthList = managerAuthService.findAllByAuthLevel(authLevelValue); // 해당 AuthLevel의 ManagerAuth 가져오기

        // 중복 키 처리 추가
        Map<Long, ManagerAuth> managerAuthMap = managerAuthList.stream()
                .collect(Collectors.toMap(ManagerAuth::getMenuIdx, Function.identity(), (existing, replacement) -> {
                    // 중복된 경우 하나를 선택하거나, 필요한 로직으로 처리합니다.
                    log.warn("중복된 menuIdx 발견: {}", existing.getMenuIdx());
                    // 여기서는 기존 값을 유지합니다.
                    return existing;
                }));

        // 모델에 추가
        model.addAttribute("authLevelDetail", resDTO);
        model.addAttribute("menuList", menuList);
        model.addAttribute("menuListAsJson", objectMapper.writeValueAsString(menuList));
        model.addAttribute("managerAuthMap", managerAuthMap);

        // TODO 페이지 제작
        return null;
    }

    /* 관리자 권한 등록 페이지 */
    @GetMapping("/save")
    public String saveForm(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws JsonProcessingException {

        return "/masterpage_sys/auth_level/saveForm";
    }

}
