package com.example.junguniv_bb.domain.survey._quest.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyQuestRepository extends JpaRepository<SurveyQuest, Long> {
    /* 검색 */
    Page<SurveyQuest> findByQuestTypeContainingIgnoreCase(@Param("questType") String questType, @Param("pageable") Pageable pageable);

    /* 설문 IDX로 삭제 */
    void deleteBySurveyIdx(@Param("surveyIdx") Long surveyIdx);

    /* 설문 IDX 목록으로 삭제 */
    void deleteBySurveyIdxIn(@Param("surveyIdxList") List<Long> surveyIdxList);
}
