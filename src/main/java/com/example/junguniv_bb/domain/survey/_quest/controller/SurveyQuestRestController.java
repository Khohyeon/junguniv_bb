package com.example.junguniv_bb.domain.survey._quest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.survey._quest.dto.SurveyQuestDetailResDTO;
import com.example.junguniv_bb.domain.survey._quest.dto.SurveyQuestPageResDTO;
import com.example.junguniv_bb.domain.survey._quest.dto.SurveyQuestSaveReqDTO;
import com.example.junguniv_bb.domain.survey._quest.dto.SurveyQuestUpdateReqDTO;
import com.example.junguniv_bb.domain.survey._quest.service.SurveyQuestService;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage_pro/survey/quest/api")
@Slf4j
public class SurveyQuestRestController {

    /* DI */
    private final SurveyQuestService surveyQuestService;

    /* 페이징 */
    @GetMapping
    public ResponseEntity<?> surveyQuestPage(Pageable pageable, @RequestParam(required = false) String questType) {
        // 페이징 및 검색 처리
        SurveyQuestPageResDTO resDTO = surveyQuestService.surveyQuestPage(pageable, questType);

        // 반환
        return ResponseEntity.ok(APIUtils.success(resDTO));
    }

    /* 다중삭제 */
    @DeleteMapping("/multi_delete")
    public ResponseEntity<?> surveyQuestDeleteAll(@RequestBody List<Long> ids) {
        // 다중 삭제 처리
        surveyQuestService.surveyQuestDeleteAll(ids);

        // 반환
        return ResponseEntity.ok(APIUtils.success("설문평가 문항 다중 삭제 완료."));
    }

    /* 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> surveyQuestDelete(@PathVariable Long id) {
        // 삭제 처리
        surveyQuestService.surveyQuestDelete(id);

        // 반환
        return ResponseEntity.ok(APIUtils.success("설문평가 문항 삭제 완료."));
    }

    /* 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<?> surveyQuestUpdate(@PathVariable Long id, @Valid @RequestBody SurveyQuestUpdateReqDTO reqDTO) {
        // 수정 처리
        surveyQuestService.surveyQuestUpdate(id, reqDTO);

        // 반환
        return ResponseEntity.ok(APIUtils.success("설문평가 문항 수정 완료."));
    }

    /* 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<?> surveyQuestDetail(@PathVariable Long id) {
        // 상세 조회
        SurveyQuestDetailResDTO resDTO = surveyQuestService.surveyQuestDetail(id);

        // 반환
        return ResponseEntity.ok(APIUtils.success(resDTO));
    }

    /* 등록 */
    @PostMapping
    public ResponseEntity<?> surveyQuestSave(@Valid @RequestBody SurveyQuestSaveReqDTO reqDTO) {
        // 등록 처리
        surveyQuestService.surveyQuestSave(reqDTO);

        // 반환
        return ResponseEntity.ok(APIUtils.success("설문평가 문항 등록 완료."));
    }
}
