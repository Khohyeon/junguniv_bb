package com.example.junguniv_bb.domain.counsel.controller;

import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.counsel.dto.CounselSaveReqDTO;
import com.example.junguniv_bb.domain.counsel.dto.CounselSearchResDTO;
import com.example.junguniv_bb.domain.counsel.dto.CounselUpdateReqDTO;
import com.example.junguniv_bb.domain.counsel.service.CounselService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/counsel/api")
public class CounselRestController {

    private final CounselService counselService;

    @GetMapping("/search")
    public ResponseEntity<APIUtils.APIResult<Page<CounselSearchResDTO>>> searchCounsels(
            @RequestParam String counselName,
            @RequestParam Integer counselState,
            Pageable pageable) {

        // 팝업의 페이징 형태를 counselIdx 기준으로 DESC 내림차순으로 설정
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "counselIdx"));

        Page<CounselSearchResDTO> searchResults = counselService.searchCounselsByName(counselName, counselState, pageable);
        return ResponseEntity.ok(APIUtils.success(searchResults));

    }


    /**
     *  [관리자모드] 홈페이지관리 - 문의상담관리 - 상담예약 - 상담명 등록페이지 - 등록하기
     */
    @PostMapping("/save")
    public ResponseEntity<APIUtils.APIResult<String>> counselSave(@ModelAttribute CounselSaveReqDTO counselSaveReqDTO) {
        counselService.counselSave(counselSaveReqDTO);
        return ResponseEntity.ok(APIUtils.success("상담예약이 성공적으로 완료되었습니다."));
    }

    /**
     *  [관리자모드] 홈페이지관리 - 문의상담관리 - 상담예약 - 상담 상세페이지 - 수정하기
     */
    @PutMapping("/update")
    public ResponseEntity<APIUtils.APIResult<String>> counselUpdate(@ModelAttribute CounselUpdateReqDTO counselUpdateReqDTO) {
        counselService.counselUpdate(counselUpdateReqDTO);
        return ResponseEntity.ok(APIUtils.success("상담예약이 성공적으로 완료되었습니다."));
    }

    /**
     *  [관리자모드] 홈페이지관리 - 문의상담관리 - 상담예약 목록페이지 - 삭제하기
     *  체크박스에 체크된 팝업 일괄 삭제
     */
    @DeleteMapping("/delete")
    public ResponseEntity<APIUtils.APIResult<String>> counselDelete(@RequestBody List<Long> counselIds) {
        counselService.counselListDelete(counselIds);
        return ResponseEntity.ok(APIUtils.success("상담예약삭제가 성공적으로 완료되었습니다."));
    }
}
