package com.example.junguniv_bb.domain.authLevel.controller;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.authLevel.dto.AuthLevelDetailResDTO;
import com.example.junguniv_bb.domain.authLevel.dto.AuthLevelPageResDTO;
import com.example.junguniv_bb.domain.authLevel.dto.AuthLevelSaveReqDTO;
import com.example.junguniv_bb.domain.authLevel.dto.AuthLevelUpdateReqDTO;
import com.example.junguniv_bb.domain.authLevel.service.AuthLevelService;
import com.example.junguniv_bb.domain.member.model.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterpage_sys/auth_level/api")
@RequiredArgsConstructor
@Slf4j
public class AuthLevelRestController {

    /* DI */
    private final AuthLevelService authLevelService;

    /* 관리자 권한 페이징 및 검색 */
    @GetMapping
    public ResponseEntity<?> authLevelPage(Member member, Pageable pageable, String authLevelName) {

        // TODO 권한 체크

        // 서비스 호출
        AuthLevelPageResDTO resDTO = authLevelService.authLevelPage(member, pageable, authLevelName);

        return ResponseEntity.ok(APIUtils.success(resDTO));
    }

    /* 관리자 권한 멀티 삭제 */
    @DeleteMapping
    public ResponseEntity<?> authLevelDeleteMulti(@RequestBody List<Long> ids) {
        for (Long id : ids) {
            authLevelService.authLevelDelete(id);
        }

        return ResponseEntity.ok(APIUtils.success("관리자 권한 다중 삭제 완료."));
    }

    /* 관리자 권한 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> authLevelDelete(@PathVariable Long id) {

        try {
            // 서비스 호출
            authLevelService.authLevelDelete(id);
            return ResponseEntity.ok(APIUtils.success("관리자 권한 삭제 완료."));
        } catch (Exception400 e) {
            // 예외 처리 로직 (예: 400 Bad Request 반환)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(APIUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST));
        }
    }

    /* 관리자 권한 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<?> authLevelUpdate(@PathVariable Long id, @RequestBody @Valid AuthLevelUpdateReqDTO reqDTO,
            Errors errors) {

        if (errors.hasErrors()) {
            log.warn(errors.getAllErrors()
                    .get(0)
                    .getDefaultMessage());
            throw new Exception400(errors.getAllErrors()
                    .get(0)
                    .getDefaultMessage());
        }

        // 서비스 호출
        authLevelService.authLevelUpdate(id, reqDTO);

        return ResponseEntity.ok(APIUtils.success(reqDTO));

    }

    /* 관리자 권한 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<?> authLevelDetail(@PathVariable Long id) {

        // 서비스 호출
        AuthLevelDetailResDTO resDTO = authLevelService.authLevelDetail(id);

        return ResponseEntity.ok(APIUtils.success(resDTO));
    }

    /* 관리자 권한 등록 */
    @PostMapping
    public ResponseEntity<?> authLevelSave(@RequestBody @Valid AuthLevelSaveReqDTO reqDTO, Errors errors) {

        if (errors.hasErrors()) {
            log.warn(errors.getAllErrors()
                    .get(0)
                    .getDefaultMessage());
            throw new Exception400(errors.getAllErrors()
                    .get(0)
                    .getDefaultMessage());
        }

        // 서비스 호출
        authLevelService.authLevelSave(reqDTO);

        return ResponseEntity.ok(APIUtils.success("관리자 권한 등록 완료."));
    }
}
