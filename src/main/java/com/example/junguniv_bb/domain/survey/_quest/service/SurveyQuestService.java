package com.example.junguniv_bb.domain.survey._quest.service;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.junguniv_bb.domain.survey._quest.dto.SurveyQuestDetailResDTO;
import com.example.junguniv_bb.domain.survey._quest.dto.SurveyQuestPageResDTO;
import com.example.junguniv_bb.domain.survey._quest.dto.SurveyQuestSaveReqDTO;
import com.example.junguniv_bb.domain.survey._quest.dto.SurveyQuestUpdateReqDTO;
import com.example.junguniv_bb.domain.survey._quest.model.SurveyQuest;
import com.example.junguniv_bb.domain.survey._quest.model.SurveyQuestRepository;
import com.example.junguniv_bb.domain.survey.model.SurveyRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SurveyQuestService {

    /* DI */
    private final SurveyQuestRepository surveyQuestRepository;
    private final SurveyRepository surveyRepository;

    /* 페이징 및 검색 */
    public SurveyQuestPageResDTO surveyQuestPage(Pageable pageable, String questType) {
        // 초기화
        Page<SurveyQuest> surveyQuestPage;

        // 검색
        if (questType != null && !questType.isEmpty()) {
            surveyQuestPage = surveyQuestRepository.findByQuestTypeContainingIgnoreCase(questType, pageable);
        } else {
            surveyQuestPage = surveyQuestRepository.findAll(pageable);
        }

        // 반환
        return SurveyQuestPageResDTO.from(surveyQuestPage);
    }

    /* 다중삭제 */
    @Transactional
    public void surveyQuestDeleteAll(List<Long> ids) {
        // 유효성 검사
        if (ids == null || ids.isEmpty()) {
            throw new Exception400(ExceptionMessage.INVALID_INPUT_VALUE.getMessage());
        }

        // 존재하는 설문 문항 조회
        List<SurveyQuest> existingSurveyQuests = surveyQuestRepository.findAllById(ids);
        
        // 요청된 ID 개수와 조회된 설문 문항 개수가 다르면 존재하지 않는 ID가 포함된 것
        if (existingSurveyQuests.size() != ids.size()) {
            throw new Exception400(ExceptionMessage.NOT_FOUND_SURVEY_QUEST.getMessage());
        }

        // 엔티티 삭제
        surveyQuestRepository.deleteAllById(ids);
    }

    /* 삭제 */
    @Transactional
    public void surveyQuestDelete(Long id) {

        // 조회 및 존재 여부 검사
        SurveyQuest surveyQuestPS = surveyQuestRepository.findById(id)
            .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_SURVEY_QUEST.getMessage()));

        // 엔티티 삭제
        surveyQuestRepository.delete(surveyQuestPS);
    }

    /* 수정 */
    @Transactional
    public void surveyQuestUpdate(Long id, SurveyQuestUpdateReqDTO reqDTO) {
        // 조회 및 존재 여부 검사
        SurveyQuest surveyQuestPS = surveyQuestRepository.findById(id)
            .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_SURVEY_QUEST.getMessage()));

        // DTO 데이터로 엔티티 필드 업데이트
        reqDTO.updateEntity(surveyQuestPS);
    }

    /* 조회 */
    public SurveyQuestDetailResDTO surveyQuestDetail(Long id) {
        // 조회 및 존재 여부 검사
        SurveyQuest surveyQuestPS = surveyQuestRepository.findById(id)
            .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_SURVEY_QUEST.getMessage()));

        // 반환
        return SurveyQuestDetailResDTO.from(surveyQuestPS);
    }

    /* 등록 */
    @Transactional
    public void surveyQuestSave(SurveyQuestSaveReqDTO reqDTO) {
        // 설문 존재 여부 검사
        surveyRepository.findById(reqDTO.surveyIdx())
            .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_SURVEY.getMessage()));

        // DTO 데이터로 엔티티 생성 및 저장
        surveyQuestRepository.save(reqDTO.toEntity());
    }
}
