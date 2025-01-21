package com.example.junguniv_bb.domain.managerMenu.controller;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.managerMenu.dto.ManagerMenuDetailResDTO;
import com.example.junguniv_bb.domain.managerMenu.dto.ManagerMenuUpdateReqDTO;
import com.example.junguniv_bb.domain.managerMenu.dto.ManagerMenuSaveReqDTO;
import com.example.junguniv_bb.domain.managerMenu.dto.ManagerMenuPageResDTO;
import com.example.junguniv_bb.domain.managerMenu.service.ManagerMenuService;
import com.example.junguniv_bb.domain.managerMenu.dto.ManagerMenuDepth3ListResDTO;
import com.example.junguniv_bb.domain.member.model.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /* ManagerMenu 다중 삭제 */
    @DeleteMapping
    public ResponseEntity<?> managerMenuDeleteMulti(@RequestBody List<Long> ids) {

        // 서비스 호출
        managerMenuService.managerMenuDeleteMultiple(ids);

        return ResponseEntity.ok(APIUtils.success("메뉴 다중 삭제 완료."));
    }

    /* ManagerMenu 페이징 및 검색 */
    @GetMapping
    public ResponseEntity<?> managerMenuPage(Member member, Pageable pageable, String menuName) {
        
        // 서비스 호출
        ManagerMenuPageResDTO resDTO = managerMenuService.managerMenuPage(member, pageable, menuName);

        return ResponseEntity.ok(APIUtils.success(resDTO));
    }

    /* ManagerMenu 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> managerMenuDelete(@PathVariable Long id) {

        // 서비스 호출
        managerMenuService.managerMenuDelete(id);

        return ResponseEntity.ok(APIUtils.success("메뉴 삭제 완료."));
    }

    /* ManagerMenu 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<?> managerMenuUpdate(@PathVariable Long id, @RequestBody @Valid ManagerMenuUpdateReqDTO reqDTO,
            Errors errors) {

        if (errors.hasErrors()) {
            log.warn(errors.getAllErrors().get(0).getDefaultMessage());
            throw new Exception400(errors.getAllErrors().get(0).getDefaultMessage());
        }

        // 서비스 호출
        managerMenuService.managerMenuUpdate(id, reqDTO);

        return ResponseEntity.ok(APIUtils.success("메뉴 수정 완료."));
    }

    /* ManagerMenu 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<?> managerMenuDetail(@PathVariable Long id) {
        ManagerMenuDetailResDTO resDTO = managerMenuService.managerMenuDetail(id);
        return ResponseEntity.ok(APIUtils.success(resDTO));
    }

    /* ManagerMenu 등록 */
    @PostMapping
    public ResponseEntity<?> managerMenuSave(@RequestBody @Valid ManagerMenuSaveReqDTO reqDTO, Errors errors) {

        if (errors.hasErrors()) {
            log.warn(errors.getAllErrors().get(0).getDefaultMessage());
            throw new Exception400(errors.getAllErrors().get(0).getDefaultMessage());
        }

        // 서비스 호출
        managerMenuService.managerMenuSave(reqDTO);

        return ResponseEntity.ok(APIUtils.success("메뉴 등록 완료."));
    }
}
