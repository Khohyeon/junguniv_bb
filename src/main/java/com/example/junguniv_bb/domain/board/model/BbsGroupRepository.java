package com.example.junguniv_bb.domain.board.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BbsGroupRepository extends JpaRepository<BbsGroup, Long> {
    BbsGroup findByBbsGroupName(String bbsGroupName);
}
