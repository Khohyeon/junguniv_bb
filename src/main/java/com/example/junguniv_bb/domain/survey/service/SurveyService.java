package com.example.junguniv_bb.domain.survey.service;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.junguniv_bb.domain.survey.dto.SurveyDetailResDTO;
import com.example.junguniv_bb.domain.survey.dto.SurveyPageResDTO;
import com.example.junguniv_bb.domain.survey.dto.SurveySaveReqDTO;
import com.example.junguniv_bb.domain.survey.dto.SurveyUpdateReqDTO;
import com.example.junguniv_bb.domain.survey.model.Survey;
import com.example.junguniv_bb.domain.survey.model.SurveyRepository;
import com.example.junguniv_bb.domain.survey._quest.model.SurveyQuestRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SurveyService {

    /* DI */
    private final SurveyRepository surveyRepository;
    private final SurveyQuestRepository surveyQuestRepository;

    /* 페이징 및 검색 */
    public SurveyPageResDTO surveyPage(Pageable pageable, String surveyTitle) {

        // 초기화
        Page<Survey> surveyPage;

        // 검색
        if (surveyTitle != null && !surveyTitle.isEmpty()) {
            surveyPage = surveyRepository.findBySurveyTitleContainingIgnoreCase(surveyTitle, pageable);
        } else {
            surveyPage = surveyRepository.findAll(pageable);
        }

        // 반환
        return SurveyPageResDTO.from(surveyPage);
    }


    /* 다중삭제 */
    @Transactional
    public void surveyDeleteAll(List<Long> ids) {
        // 유효성 검사
        if (ids == null || ids.isEmpty()) {
            throw new Exception400(ExceptionMessage.INVALID_INPUT_VALUE.getMessage());
        }

        // 존재하는 설문 조회
        List<Survey> existingSurveys = surveyRepository.findAllById(ids);
        
        // 요청된 ID 개수와 조회된 설문 개수가 다르면 존재하지 않는 ID가 포함된 것
        if (existingSurveys.size() != ids.size()) {
            throw new Exception400(ExceptionMessage.NOT_FOUND_SURVEY.getMessage());
        }

        // 연관된 설문 문항 삭제
        surveyQuestRepository.deleteBySurveyIdxIn(ids);

        // 엔티티 삭제
        surveyRepository.deleteAllById(ids);
    }

    /* 삭제 */
    @Transactional
    public void surveyDelete(Long id) {
        // 조회 및 존재 여부 검사
        Survey surveyPS = surveyRepository.findById(id)
            .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_SURVEY.getMessage()));

        // 연관된 설문 문항 삭제
        surveyQuestRepository.deleteBySurveyIdx(id);

        // 엔티티 삭제
        surveyRepository.delete(surveyPS);
    }

    /* 수정 */
    @Transactional
    public void surveyUpdate(Long id, SurveyUpdateReqDTO reqDTO) {

        // 조회 및 존재 여부 검사
        Survey surveyPS = surveyRepository.findById(id)
            .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_SURVEY.getMessage()));


        // DTO 데이터로 엔티티 필드 업데이트
        reqDTO.updateEntity(surveyPS);

    }

    /* 조회 */
    public SurveyDetailResDTO surveyDetail(Long id) {

        // 조회 및 존재 여부 검사
        Survey surveyPS = surveyRepository.findById(id)
            .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_SURVEY.getMessage()));


        // 반환
        return SurveyDetailResDTO.from(surveyPS);
    }

    /* 등록 */
    @Transactional
    public void surveySave(SurveySaveReqDTO reqDTO) {

        // DTO 데이터로 엔티티 생성 및 저장
        surveyRepository.save(reqDTO.toEntity());
    }

}
