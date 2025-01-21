package com.example.junguniv_bb.domain.counsel.controller;

import com.example.junguniv_bb.domain.counsel.service.CounselService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/counsel")
public class CounselController {

    private final CounselService counselService;

    /**
     *  [관리자모드] 홈페이지관리 - 문의상담관리 - 문의상담 목록페이지
     *  Model 응답 Page<CounselPageResDTO>
     */
    @GetMapping
    public String counselListForm(Pageable pageable, Model model) {

        // 상담의 페이징 형태를 counselIdx 기준으로 DESC 내림차순으로 설정
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "counselIdx"));

        // Service 에서 counsel 을 Page 형태로 응답받아서 모델에 담음
        model.addAttribute("counselPage", counselService.getCounselPage(pageable));

        return "/masterpage_sys/counsel/listForm";
    }

    /**
     *  [관리자모드] 홈페이지관리 - 문의상담관리 - 문의상담 - 상담상태 클릭 시 상세페이지
     */
    @GetMapping("/{counselIdx}")
    public String counselDetailForm(@PathVariable Long counselIdx, Model model) {

        // 문의상담 List 에서 counselIdx를 가져와 팝업 데이터 가져와 model에 담아 보내기
        model.addAttribute("counsel", counselService.counselDetail(counselIdx));

        return "masterpage_sys/counsel/detailForm";
    }

    /**
     *  [관리자모드] 홈페이지관리 - 문의상담관리 - 문의상담 - 상담 예약페이지
     */
    @GetMapping("/save")
    public String counselSaveForm() {
        return "/masterpage_sys/counsel/saveForm";
    }

}
