package com.example.junguniv_bb.domain.managermenu._branch.controller;

import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.managermenu._branch.dto.Depth1MenuSaveReqDTO;
import com.example.junguniv_bb.domain.managermenu.service.ManagerMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
