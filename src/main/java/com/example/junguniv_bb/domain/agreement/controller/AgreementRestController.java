package com.example.junguniv_bb.domain.agreement.controller;

import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.agreement.dto.AgreementJoinSaveReqDTO;
import com.example.junguniv_bb.domain.agreement.dto.AgreementJoinUpdateReqDTO;
import com.example.junguniv_bb.domain.agreement.dto.AgreementSaveReqDTO;
import com.example.junguniv_bb.domain.agreement.dto.AgreementUpdateReqDTO;
import com.example.junguniv_bb.domain.agreement.service.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/agreement/api")
public class AgreementRestController {

    private final AgreementService agreementService;

    /**
     *  [관리자모드] 홈페이지관리 - 약관관리 - 회원가입약관 - 약관 상세페이지 - 수정하기
     */
    @PutMapping("/update")
    public ResponseEntity<APIUtils.APIResult<String>> agreementUpdate(@ModelAttribute AgreementJoinUpdateReqDTO agreementJoinUpdateReqDTO) {
        agreementService.agreementUpdate(agreementJoinUpdateReqDTO);
        return ResponseEntity.ok(APIUtils.success("회원약관수정이 성공적으로 완료되었습니다."));
    }

    /**
     *  [관리자모드] 홈페이지관리 - 약관관리 - 수강신청약관/환불신청약관 - 수정하기
     */
    @PutMapping("/join/save")
    public ResponseEntity<APIUtils.APIResult<String>> agreementJoinSave(@RequestBody AgreementJoinSaveReqDTO agreementJoinSaveReqDTO) {
        agreementService.joinUpdate(agreementJoinSaveReqDTO);
        return ResponseEntity.ok(APIUtils.success("교육원 수정이 성공적으로 완료되었습니다."));
    }


    /**
     *  [관리자모드] 홈페이지관리 - 약관관리 - 수강신청약관/환불신청약관 - 수정하기
     */
    @PutMapping("/save")
    public ResponseEntity<APIUtils.APIResult<String>> agreementSave(@RequestBody AgreementSaveReqDTO agreementSaveReqDTO) {
        agreementService.courseUpdate(agreementSaveReqDTO);
        return ResponseEntity.ok(APIUtils.success("사용여부 수정이 성공적으로 완료되었습니다."));
    }

    /**
     *  [관리자모드] 홈페이지관리 - 약관관리 - 수강신청약관/환불신청약관 - 약관 상세페이지 - 수정하기
     */
    @PutMapping("/update2")
    public ResponseEntity<APIUtils.APIResult<String>> agreementUpdate2(
            @RequestBody List<AgreementUpdateReqDTO> agreementUpdateReqDTOList) {

        agreementService.agreementUpdate2(agreementUpdateReqDTOList);
        return ResponseEntity.ok(APIUtils.success("약관수정이 성공적으로 완료되었습니다."));
    }

    @DeleteMapping("/delete/{agreementIdx}")
    public ResponseEntity<APIUtils.APIResult<String>> deleteAgreement(@PathVariable Long agreementIdx) {
        agreementService.deleteAgreement(agreementIdx);
        return ResponseEntity.ok(APIUtils.success("항목이 성공적으로 삭제되었습니다."));
    }


}
