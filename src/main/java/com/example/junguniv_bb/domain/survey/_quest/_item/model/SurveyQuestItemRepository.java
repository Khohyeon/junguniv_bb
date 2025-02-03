package com.example.junguniv_bb.domain.survey._quest._item.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyQuestItemRepository extends JpaRepository<SurveyQuestItem, Long> {
}
