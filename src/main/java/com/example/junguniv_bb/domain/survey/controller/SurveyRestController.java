package com.example.junguniv_bb.domain.survey.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.survey.dto.SurveyDetailResDTO;
import com.example.junguniv_bb.domain.survey.dto.SurveyPageResDTO;
import com.example.junguniv_bb.domain.survey.dto.SurveySaveReqDTO;
import com.example.junguniv_bb.domain.survey.dto.SurveyUpdateReqDTO;
import com.example.junguniv_bb.domain.survey.service.SurveyService;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage_pro/survey/api")
@Slf4j
public class SurveyRestController {

    /* DI */
    private final SurveyService surveyService;

    /* 페이징 */
    @GetMapping
    public ResponseEntity<?> surveyPage(Pageable pageable, @RequestParam(required = false) String surveyTitle) {

        // 서비스 호출
        SurveyPageResDTO resDTO = surveyService.surveyPage(pageable, surveyTitle);
        
        return ResponseEntity.ok(APIUtils.success(resDTO));
    }

    /* 다중삭제 */
    @DeleteMapping("/multi_delete")
    public ResponseEntity<?> surveyDeleteAll(@RequestBody List<Long> ids) {

        // 서비스 호출
        surveyService.surveyDeleteAll(ids);

        return ResponseEntity.ok(APIUtils.success("설문평가 다중 삭제 완료."));
    }

    /* 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> surveyDelete(@PathVariable Long id) {

        // 서비스 호출
        surveyService.surveyDelete(id);

        return ResponseEntity.ok(APIUtils.success("설문평가 삭제 완료."));

    }

    /* 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<?> surveyUpdate(@PathVariable Long id, @Valid @RequestBody SurveyUpdateReqDTO reqDTO) {

        // 서비스 호출
        surveyService.surveyUpdate(id, reqDTO);

        return ResponseEntity.ok(APIUtils.success("설문평가 수정 완료."));
    }

    /* 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<?> surveyDetail(@PathVariable Long id) {

        // 서비스 호출
        SurveyDetailResDTO resDTO = surveyService.surveyDetail(id);

        return ResponseEntity.ok(APIUtils.success(resDTO));
    }

    /* 등록 */
    @PostMapping
    public ResponseEntity<?> surveySave(@Valid @RequestBody SurveySaveReqDTO reqDTO) {

        // 서비스 호출
        surveyService.surveySave(reqDTO);

        return ResponseEntity.ok(APIUtils.success("설문평가 등록 완료."));
    }
}
