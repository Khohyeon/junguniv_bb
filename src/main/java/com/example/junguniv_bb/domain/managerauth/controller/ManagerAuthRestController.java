package com.example.junguniv_bb.domain.managerauth.controller;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.managerauth.dto.ManagerAuthDetailResDTO;
import com.example.junguniv_bb.domain.managerauth.dto.ManagerAuthPageResDTO;
import com.example.junguniv_bb.domain.managerauth.dto.ManagerAuthSaveReqDTO;
import com.example.junguniv_bb.domain.managerauth.dto.ManagerAuthUpdateReqDTO;
import com.example.junguniv_bb.domain.managerauth.service.ManagerAuthService;
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
@RequestMapping("/masterpage_sys/manager_auth/api")
@Slf4j
public class

ManagerAuthRestController {

    /* DI */
    private final ManagerAuthService managerAuthService;

    /* ManagerAuth 업서트 (업데이트 + 삽입) */
    @PutMapping("/upsert")
    public ResponseEntity<?> managerAuthUpsert(@RequestBody @Valid ManagerAuthUpdateReqDTO reqDTO, Errors errors) {

        if (errors.hasErrors()) {
            log.warn(errors.getAllErrors().get(0).getDefaultMessage());
            throw new Exception400(errors.getAllErrors().get(0).getDefaultMessage());
        }

        // 서비스 호출
        managerAuthService.managerAuthUpsert(reqDTO);

        return ResponseEntity.ok(APIUtils.success("메뉴 권한 upsert 완료."));
    }


    /* ManagerAuth 페이징 및 검색 */
    @GetMapping
    public ResponseEntity<?> managerAuthPage(Member member, Pageable pageable, String menuName) {

        // TODO 권한 체크

        // 서비스 호출
        ManagerAuthPageResDTO resDTO = managerAuthService.managerAuthPage(member, pageable, menuName);

        return ResponseEntity.ok(APIUtils.success(resDTO));
    }


    /* ManagerAuth 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> managerAuthDelete(@PathVariable Long id) {

        // 서비스 호출
        managerAuthService.managerAuthDelete(id);

        return ResponseEntity.ok(APIUtils.success("메뉴 권한 삭제 완료."));
    }


    /* ManagerAuth 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<?> managerAuthUpdate(@PathVariable Long id, @RequestBody @Valid ManagerAuthUpdateReqDTO reqDTO, Errors errors) {

        if (errors.hasErrors()) {
            log.warn(errors.getAllErrors()
                    .get(0)
                    .getDefaultMessage());
            throw new Exception400(errors.getAllErrors()
                    .get(0)
                    .getDefaultMessage());
        }

        // 서비스 호출
        managerAuthService.managerAuthUpdate(id, reqDTO);

        return ResponseEntity.ok(APIUtils.success("메뉴 권한 수정 완료."));
    }



    /* ManagerAuth 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<?> managerAuthDetail(@PathVariable Long id) {
        // 서비스 호출
        ManagerAuthDetailResDTO resDTO = managerAuthService.managerAuthDetail(id);

        return ResponseEntity.ok(APIUtils.success(resDTO));
    }


    /* ManagerAuth 등록 */
    @PostMapping
    public ResponseEntity<?> managerAuthSave(@RequestBody @Valid List<ManagerAuthSaveReqDTO> reqDTOList, Errors errors) {

        if (errors.hasErrors()) {
            log.warn(errors.getAllErrors()
                    .get(0)
                    .getDefaultMessage());
            throw new Exception400(errors.getAllErrors()
                    .get(0)
                    .getDefaultMessage());
        }

        // 서비스 호출
        managerAuthService.managerAuthSave(reqDTOList);

        return ResponseEntity.ok(APIUtils.success("메뉴 권한 등록 완료."));
    }

}
