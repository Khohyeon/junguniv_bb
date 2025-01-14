package com.example.junguniv_bb.domain.popup.controller;

import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.popup.dto.PopupSaveReqDTO;
import com.example.junguniv_bb.domain.popup.dto.PopupUpdateReqDTO;
import com.example.junguniv_bb.domain.popup.service.PopupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("masterpage_sys/popup/api")
@RequiredArgsConstructor
public class PopupRestController {

    private final PopupService popupService;

    /**
     *  [관리자모드] 홈페이지관리 - 팝업관리 - 메인팝업 - 팝업 등록페이지 - 등록하기
     */
    @PostMapping("/save")
    public ResponseEntity<APIUtils.APIResult<String>> popupSave(@ModelAttribute PopupSaveReqDTO popupSaveReqDTO) {
        popupService.popupSave(popupSaveReqDTO);
        return ResponseEntity.ok(APIUtils.success("팝업등록이 성공적으로 완료되었습니다."));
    }

    /**
     *  [관리자모드] 홈페이지관리 - 팝업관리 - 메인팝업 - 팝업 상세페이지 - 수정하기
     */
    @PutMapping("/update")
    public ResponseEntity<APIUtils.APIResult<String>> popupUpdate(@ModelAttribute PopupUpdateReqDTO popupUpdateReqDTO) {
        popupService.popupUpdate(popupUpdateReqDTO);
        return ResponseEntity.ok(APIUtils.success("팝업수정이 성공적으로 완료되었습니다."));
    }

    /**
     *  [관리자모드] 홈페이지관리 - 팝업관리 - 메인팝업 - 삭제하기
     *  체크박스에 체크된 팝업 일괄 삭제
     */
    @DeleteMapping("/delete")
    public ResponseEntity<APIUtils.APIResult<String>> popupDelete(@RequestBody List<Long> popupIds) {
        popupService.popupListDelete(popupIds);
        return ResponseEntity.ok(APIUtils.success("팝업삭제가 성공적으로 완료되었습니다."));
    }
}
