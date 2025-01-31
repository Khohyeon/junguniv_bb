package com.example.junguniv_bb.domain.popup.controller;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.popup.dto.PopupSaveReqDTO;
import com.example.junguniv_bb.domain.popup.dto.PopupSearchResDTO;
import com.example.junguniv_bb.domain.popup.dto.PopupUpdateReqDTO;
import com.example.junguniv_bb.domain.popup.service.PopupService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;


@RestController
@RequestMapping("masterpage_sys/popup/api")
@RequiredArgsConstructor
public class PopupRestController {

    private final PopupService popupService;

    @GetMapping("/search")
    public ResponseEntity<APIUtils.APIResult<Page<PopupSearchResDTO>>> searchPopups(@RequestParam String popupName, Pageable pageable) {

        // 팝업의 페이징 형태를 popupIdx 기준으로 DESC 내림차순으로 설정
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "popupIdx"));

        Page<PopupSearchResDTO> searchResults = popupService.searchPopupsByName(popupName, pageable);
        return ResponseEntity.ok(APIUtils.success(searchResults));

    }

    /**
     *  [관리자모드] 홈페이지관리 - 팝업관리 - 메인팝업 - 팝업 등록페이지 - 등록하기
     */
    @PostMapping("/save")
    public ResponseEntity<APIUtils.APIResult<String>> popupSave(@Valid @ModelAttribute PopupSaveReqDTO popupSaveReqDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new Exception400(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        popupService.popupSave(popupSaveReqDTO);
        return ResponseEntity.ok(APIUtils.success("팝업등록이 성공적으로 완료되었습니다."));
    }

    /**
     *  [관리자모드] 홈페이지관리 - 팝업관리 - 메인팝업 - 팝업 상세페이지 - 수정하기
     */
    @PutMapping("/update")
    public ResponseEntity<APIUtils.APIResult<String>> popupUpdate(@Valid @ModelAttribute PopupUpdateReqDTO popupUpdateReqDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new Exception400(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

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
