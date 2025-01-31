package com.example.junguniv_bb.domain.systemcode.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.systemcode.dto.SystemCodeDetailResDTO;
import com.example.junguniv_bb.domain.systemcode.dto.SystemCodePageResDTO;
import com.example.junguniv_bb.domain.systemcode.dto.SystemCodeSaveReqDTO;
import com.example.junguniv_bb.domain.systemcode.dto.SystemCodeUpdateReqDTO;
import com.example.junguniv_bb.domain.systemcode.service.SystemCodeService;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/system_code/api")
@Slf4j
public class SystemCodeRestController {

    /* DI */
    private final SystemCodeService systemCodeService;

    /* 페이징 및 검색 */
    @GetMapping
    public ResponseEntity<?> systemCodePage(Pageable pageable,
            @RequestParam(required = false) String systemCodeName) {

        SystemCodePageResDTO resDTO = systemCodeService.systemCodePage(pageable, systemCodeName);

        return ResponseEntity.ok(APIUtils.success(resDTO));
    }

    /* 다중삭제 */
    @DeleteMapping("/multi_delete")
    public ResponseEntity<?> systemCodeMultiDelete(@RequestBody List<Long> ids) {

        systemCodeService.systemCodeMultiDelete(ids);

        return ResponseEntity.ok(APIUtils.success("시스템 코드 다중 삭제 완료."));
    }
    
    /* 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> systemCodeDelete(@PathVariable Long id) {

        systemCodeService.systemCodeDelete(id);

        return ResponseEntity.ok(APIUtils.success("시스템 코드 삭제 완료."));
    }

    /* 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<?> systemCodeUpdate(@PathVariable Long id, @Valid @RequestBody SystemCodeUpdateReqDTO reqDTO) {
        systemCodeService.systemCodeUpdate(id, reqDTO);
        return ResponseEntity.ok(APIUtils.success("시스템 코드 수정 완료."));
    }

    /* 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<?> systemCodeDetail(@PathVariable Long id) {

        SystemCodeDetailResDTO resDTO = systemCodeService.systemCodeDetail(id);

        return ResponseEntity.ok(APIUtils.success(resDTO));
    }
    

    /* 저장 */
    @PostMapping
    public ResponseEntity<?> systemCodeSave(@Valid @RequestBody SystemCodeSaveReqDTO reqDTO) {

        systemCodeService.systemCodeSave(reqDTO);

        return ResponseEntity.ok(APIUtils.success("시스템 코드 저장 완료."));
    }
}
