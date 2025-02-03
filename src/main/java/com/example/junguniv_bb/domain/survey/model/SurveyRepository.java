package com.example.junguniv_bb.domain.survey.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

    /* 검색 */
    Page<Survey> findBySurveyTitleContainingIgnoreCase(@Param("surveyTitle") String surveyTitle, Pageable pageable);
}
