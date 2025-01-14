package com.example.junguniv_bb.domain.counsel.controller;

import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.counsel.dto.CounselSaveReqDTO;
import com.example.junguniv_bb.domain.counsel.service.CounselService;
import com.example.junguniv_bb.domain.popup.dto.PopupSaveReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/counsel/api")
public class CounselRestController {

    private final CounselService counselService;

    /**
     *  [관리자모드] 홈페이지관리 - 문의상담관리 - 상담예약 - 상담명 등록페이지 - 등록하기
     */
    @PostMapping("/save")
    public ResponseEntity<APIUtils.APIResult<String>> popupSave(@ModelAttribute CounselSaveReqDTO counselSaveReqDTO) {
        counselService.counselSave(counselSaveReqDTO);
        return ResponseEntity.ok(APIUtils.success("상담명등록이 성공적으로 완료되었습니다."));
    }
}
