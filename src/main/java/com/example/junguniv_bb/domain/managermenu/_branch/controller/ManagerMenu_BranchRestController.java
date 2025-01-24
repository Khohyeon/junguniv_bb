package com.example.junguniv_bb.domain.managermenu._branch.controller;

import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.managermenu._branch.dto.Depth1MenuSaveReqDTO;
import com.example.junguniv_bb.domain.managermenu._branch.dto.Depth2MenuSaveReqDTO;
import com.example.junguniv_bb.domain.managermenu.dto.Depth3MenuSaveReqDTO;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;
import com.example.junguniv_bb.domain.managermenu.service.ManagerMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/branch/api")
public class ManagerMenu_BranchRestController {

    private final ManagerMenuService managerMenuService;

    /**
     *  [시스템 설정] - [메뉴분류관리] - [1차메뉴추가]
     *  1차 메뉴 저장하는 매핑
     */
    @PostMapping("/save/depth1")
    public ResponseEntity<APIUtils.APIResult<String>> saveDepth1Menus(
            @RequestBody Depth1MenuSaveReqDTO depth1MenuSaveReqDTO
    ) {
        managerMenuService.saveDepth1Menu(depth1MenuSaveReqDTO);
        return ResponseEntity.ok(APIUtils.success("1차메뉴 등록이 성공적으로 완료되었습니다."));
    }

    /**
     *  [시스템 설정] - [메뉴분류관리] - [2차메뉴추가]
     *  2차 메뉴 저장하는 매핑
     */
    @PostMapping("/save/depth2")
    public ResponseEntity<APIUtils.APIResult<String>> saveDepth2Menus(
            @RequestBody Depth2MenuSaveReqDTO depth2MenuSaveReqDTO
    ) {
        managerMenuService.saveDepth2Menu(depth2MenuSaveReqDTO);
        return ResponseEntity.ok(APIUtils.success("2차메뉴 등록이 성공적으로 완료되었습니다."));
    }

    /**
     *  [시스템 설정] - [메뉴분류관리] - [3차메뉴추가]
     *  3차 메뉴 저장하는 매핑
     */
    @PostMapping("/save/depth3")
    public ResponseEntity<APIUtils.APIResult<String>> saveDepth3Menus(
            @RequestBody Depth3MenuSaveReqDTO depth3MenuSaveReqDTO
    ) {
        managerMenuService.saveDepth3Menu(depth3MenuSaveReqDTO);
        return ResponseEntity.ok(APIUtils.success("3차메뉴 등록이 성공적으로 완료되었습니다."));
    }


    /**
     *  [시스템 설정] - [메뉴분류관리] - [1차메뉴삭제]
     *  1차 메뉴 삭제하는 매핑
     */
    @DeleteMapping("/delete/{menuIdx}")
    public ResponseEntity<APIUtils.APIResult<String>> deleteDepth1Menu(@PathVariable Long menuIdx) {
        managerMenuService.deleteMenuByIdx(menuIdx);
        return ResponseEntity.ok(APIUtils.success("메뉴가 삭제되었습니다."));
    }


    @GetMapping("/depth3Menus")
    public ResponseEntity<List<ManagerMenu>> getDepth3Menus(@RequestParam Long parentIdx) {
        List<ManagerMenu> depth3Menus = managerMenuService.getDepth3MenusByParentIdx(parentIdx);
        return ResponseEntity.ok(depth3Menus);
    }

}
