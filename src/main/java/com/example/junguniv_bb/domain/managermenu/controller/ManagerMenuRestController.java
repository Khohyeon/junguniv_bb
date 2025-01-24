package com.example.junguniv_bb.domain.managermenu.controller;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.managermenu.dto.*;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;
import com.example.junguniv_bb.domain.managermenu.service.ManagerMenuService;
import com.example.junguniv_bb.domain.member.model.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/manager_menu/api")
@Slf4j
public class ManagerMenuRestController {

    /* DI */
    private final ManagerMenuService managerMenuService;

    /**
     * 2차 메뉴를 선택 했을 때 Rest API 요청
     */
    @GetMapping("/depth3")
    public List<ManagerMenuDepth3ListResDTO> getDepth3Menus(@RequestParam Long parentMenuIdx) {

        return managerMenuService.getDepth3Menus(parentMenuIdx);
    }

    @DeleteMapping
    public ResponseEntity<?> managerMenuDeleteMulti(
            @RequestBody List<Long> ids) {
        managerMenuService.managerMenuDeleteMultiple(ids);
        return ResponseEntity.ok(APIUtils.success("메뉴 다중 삭제 완료."));
    }

    @GetMapping
    public ResponseEntity<?> managerMenuPage(
            @RequestParam(required = false) Member member,
            Pageable pageable,
            @RequestParam(required = false) String menuName,
            @RequestParam(required = false, defaultValue = "ADMIN_REFUND") String menuType) {
        ManagerMenuPageResDTO resDTO = managerMenuService.managerMenuPage(member, pageable, menuName, menuType);
        return ResponseEntity.ok(APIUtils.success(resDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> managerMenuDelete(
            @PathVariable Long id) {
        managerMenuService.managerMenuDelete(id);
        return ResponseEntity.ok(APIUtils.success("메뉴 삭제 완료."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> managerMenuUpdate(
            @PathVariable Long id,
            @RequestBody @Valid ManagerMenuUpdateReqDTO reqDTO,
            Errors errors) {
        if (errors.hasErrors()) {
            String errorMessage = errors.getAllErrors().get(0).getDefaultMessage();
            log.warn(errorMessage);
            throw new Exception400(errorMessage);
        }
        managerMenuService.managerMenuUpdate(id, reqDTO);
        return ResponseEntity.ok(APIUtils.success("메뉴 수정 완료."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> managerMenuDetail(
            @PathVariable Long id) {
        ManagerMenuDetailResDTO resDTO = managerMenuService.managerMenuDetail(id);
        return ResponseEntity.ok(APIUtils.success(resDTO));
    }

    @PostMapping
    public ResponseEntity<?> managerMenuSave(
            @RequestBody @Valid ManagerMenuSaveReqDTO reqDTO,
            Errors errors) {
        if (errors.hasErrors()) {
            String errorMessage = errors.getAllErrors().get(0).getDefaultMessage();
            log.warn(errorMessage);
            throw new Exception400(errorMessage);
        }
        managerMenuService.managerMenuSave(reqDTO);
        return ResponseEntity.ok(APIUtils.success("메뉴 등록 완료."));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllMenus(
            @RequestParam(required = false) String menuType) {
        List<ManagerMenu> menuList = menuType != null ? 
            managerMenuService.findAllByMenuType(menuType) : 
            managerMenuService.findAll();
        List<ManagerMenuAllResDTO> dtoList = menuList.stream()
            .map(ManagerMenuAllResDTO::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok(APIUtils.success(dtoList));
    }

    @GetMapping("/search")
    public ResponseEntity<APIUtils.APIResult<Page<ManagerMenuSearchResDTO>>> getManagerMenuSearch(
            @RequestParam String menuName,
            @RequestParam String chkUse,
            Pageable pageable) {

        // 팝업의 페이징 형태를 counselIdx 기준으로 DESC 내림차순으로 설정
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "menuIdx"));
        Page<ManagerMenuSearchResDTO> managerMenuPage = managerMenuService.getManagerMenuPage(menuName, chkUse, pageable);
        return ResponseEntity.ok(APIUtils.success(managerMenuPage));
    }
}
