package com.example.junguniv_bb.domain.survey._quest._answer.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyQuestAnswerRepository extends JpaRepository<SurveyQuestAnswer, Long> {
}
