package com.example.junguniv_bb.domain.agreement.controller;

import com.example.junguniv_bb.domain.agreement.service.AgreementService;
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
@RequestMapping("/masterpage_sys/agreement")
public class AgreementController {

    private final AgreementService agreementService;

    /**
     *  [관리자모드] 홈페이지관리 - 약관관리 - 회원가입약관 목록페이지
     *  Model 응답 Page<AgreementPageResDTO>
     */
    @GetMapping("/joinForm")
    public String joinForm(Pageable pageable, Model model) {

        // 약관의 페이징 형태를 agreementIdx 기준으로 DESC 내림차순으로 설정
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "agreementIdx"));

        // Service 에서 agreement 을 Page 형태로 응답받아서 모델에 담음
        model.addAttribute("agreementPage", agreementService.getAgreementPage(pageable));

        return "masterpage_sys/agreement/joinForm";
    }

    @GetMapping("/detailForm/{agreementIdx}")
    public String getAgreement(@PathVariable Long agreementIdx, Model model) {

        model.addAttribute("agreement", agreementService.getAgreementDetail(agreementIdx));

        return "masterpage_sys/agreement/detailForm"; // HTML 파일명 (Thymeleaf 템플릿)
    }

    @GetMapping("/courseForm")
    public String courseForm() {
        return "masterpage_sys/agreement/courseForm";
    }

    @GetMapping("/refundForm")
    public String refundForm() {
        return "masterpage_sys/agreement/refundForm";
    }
}
