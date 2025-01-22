package com.example.junguniv_bb.domain.agreement.controller;

import com.example.junguniv_bb.domain.agreement.dto.AgreementListResDTO;
import com.example.junguniv_bb.domain.agreement.service.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/agreement")
public class AgreementController {

    private final AgreementService agreementService;

    /**
     *  [관리자모드] 홈페이지관리 - 약관관리 - 회원가입약관 목록페이지
     *  Model 응답 List<AgreementJoinListResDTO>
     */
    @GetMapping("/join")
    public String joinForm(Model model) {

        // Service 에서 agreement 을 List 형태로 응답받아서 모델에 담음
        model.addAttribute("agreementList", agreementService.getAgreementJoinList());

        return "masterpage_sys/agreement/join/listForm";
    }

    @GetMapping("/join/{agreementIdx}")
    public String getAgreement(@PathVariable Long agreementIdx, Model model) {

        model.addAttribute("agreement", agreementService.getAgreementDetail(agreementIdx));

        return "masterpage_sys/agreement/join/detailForm"; // HTML 파일명 (Thymeleaf 템플릿)
    }

    @GetMapping("/course")
    public String courseListForm(Model model) {
        List<AgreementListResDTO> agreementList = agreementService.getAgreementCourseList();
        if (!agreementList.isEmpty()) {
            model.addAttribute("agreementList", agreementList);
            model.addAttribute("agreement", agreementList.get(0)); // 첫 번째 항목 데이터 (사용 여부 바인딩)
        }
        return "masterpage_sys/agreement/course/listForm";
    }

    @GetMapping("/course/detail")
    public String courseDetailForm(Model model) {
        List<AgreementListResDTO> agreementList = agreementService.getAgreementCourseList();
        // Model에 데이터 추가
        model.addAttribute("agreementList", agreementList);

        return "masterpage_sys/agreement/course/detailForm";
    }

    @GetMapping("/refund")
    public String refundListForm(Model model) {
        List<AgreementListResDTO> agreementList = agreementService.getAgreementRefundList();
        if (!agreementList.isEmpty()) {
            model.addAttribute("agreementList", agreementList);
            model.addAttribute("agreement", agreementList.get(0)); // 첫 번째 항목 데이터 (사용 여부 바인딩)
        }

        return "masterpage_sys/agreement/refund/listForm";
    }

    @GetMapping("/refund/detail")
    public String refundDetailForm(Model model) {
        List<AgreementListResDTO> agreementList = agreementService.getAgreementRefundList();

        model.addAttribute("agreementList", agreementList);

        return "masterpage_sys/agreement/refund/detailForm";
    }
}
