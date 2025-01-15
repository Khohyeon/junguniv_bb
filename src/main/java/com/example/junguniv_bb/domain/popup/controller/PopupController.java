package com.example.junguniv_bb.domain.popup.controller;

import com.example.junguniv_bb.domain.popup.service.PopupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.data.domain.Pageable;

@Controller
@RequiredArgsConstructor
@RequestMapping("masterpage_sys/popup")
public class PopupController {

    private final PopupService popupService;

    /**
     *  [관리자모드] 홈페이지관리 - 팝업관리 - 메인팝업 목록페이지
     *  Model 응답 Page<PopupPageResDTO>
     */
    @GetMapping("/listForm")
    public String popupListForm(Pageable pageable, Model model) {

        // 팝업의 페이징 형태를 popupIdx 기준으로 DESC 내림차순으로 설정
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "popupIdx"));

        // Service 에서 popup을 Page 형태로 응답받아서 모델에 담음
        model.addAttribute("popupPage", popupService.getPopupPage(pageable));

        return "masterpage_sys/popup/listForm";
    }

    /**
     *  [관리자모드] 홈페이지관리 - 팝업관리 - 메인팝업 - 팝업 등록페이지
     */
    @GetMapping("/saveForm")
    public String popupSaveForm() {return "masterpage_sys/popup/saveForm";}

    /**
     *  [관리자모드] 홈페이지관리 - 팝업관리 - 메인팝업 - 팝업명 클릭 시 상세페이지
     */
    @GetMapping("/detailForm/{popupIdx}")
    public String popupDetailForm(@PathVariable Long popupIdx, Model model) {

        // 메인팝업 List 에서 popupIdx를 가져와 팝업 데이터 가져와 model에 담아 보내기
        model.addAttribute("popup", popupService.getPopupDetail(popupIdx));

        return "masterpage_sys/popup/detailForm";
    }



}
