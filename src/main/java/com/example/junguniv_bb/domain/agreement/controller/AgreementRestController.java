package com.example.junguniv_bb.domain.agreement.controller;

import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.agreement.dto.AgreementUpdateReqDTO;
import com.example.junguniv_bb.domain.agreement.service.AgreementService;
import com.example.junguniv_bb.domain.popup.dto.PopupUpdateReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/agreement/api")
public class AgreementRestController {

    private final AgreementService agreementService;

    /**
     *  [관리자모드] 홈페이지관리 - 약관관리 - 회원가입약관 - 약관 상세페이지 - 수정하기
     */
    @PutMapping("/update")
    public ResponseEntity<APIUtils.APIResult<String>> agreementUpdate(@ModelAttribute AgreementUpdateReqDTO agreementUpdateReqDTO) {
        agreementService.agreementUpdate(agreementUpdateReqDTO);
        return ResponseEntity.ok(APIUtils.success("회원약관수정이 성공적으로 완료되었습니다."));
    }

}
