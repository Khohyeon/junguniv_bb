package com.example.junguniv_bb.domain.subject.controller;

import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.refundPrice.model.RefundPrice;
import com.example.junguniv_bb.domain.refundPrice.service.RefundPriceService;
import com.example.junguniv_bb.domain.subject.dto.SubjectStep1SaveReqDTO;
import com.example.junguniv_bb.domain.subject.dto.SubjectStep3SaveReqDTO;
import com.example.junguniv_bb.domain.subject.model.Subject;
import com.example.junguniv_bb.domain.subject.service.SubjectService;
import com.example.junguniv_bb.domain.subject.dto.SubjectStep2SaveReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage_pro/subject/api")
public class SubjectRestController {

    private final SubjectService subjectService;
    private final RefundPriceService refundPriceService;

    @PostMapping("/save1")
    public ResponseEntity<?> subjectSave1(
            @ModelAttribute SubjectStep1SaveReqDTO subjectStep1SaveReqDTO
    ) {
        Subject subject = subjectService.save1(subjectStep1SaveReqDTO);
        return ResponseEntity.ok(APIUtils.success(subject.getSubjectIdx()));
//        return ResponseEntity.ok(APIUtils.success("Step.1 기본정보설정 저장이 완료되었습니다."));
    }

    @PostMapping("/save2")
    public ResponseEntity<?> subjectSave2(
            @ModelAttribute SubjectStep2SaveReqDTO subjectStep2SaveReqDTO
    ) {
        subjectService.save2(subjectStep2SaveReqDTO);
        return ResponseEntity.ok(APIUtils.success("Step.2 심사/강사정보 저장이 완료되었습니다."));
    }

    @PostMapping("/save3")
    public ResponseEntity<?> subjectSave3(
            @ModelAttribute SubjectStep3SaveReqDTO subjectStep3SaveReqDTO
    ) {
        subjectService.save3(subjectStep3SaveReqDTO);
        return ResponseEntity.ok(APIUtils.success("Step.3 운영/평가관리 저장이 완료되었습니다."));
    }



    @GetMapping("/refundData")
    public ResponseEntity<?> refundData() {
        List<RefundPrice> refundPrices = refundPriceService.refundPriceList();
        return ResponseEntity.ok(APIUtils.success(refundPrices));
    }
}
