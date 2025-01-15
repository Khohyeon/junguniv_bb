package com.example.junguniv_bb.domain.board.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BbsRepository extends JpaRepository<Bbs, Long> {
    Page<Bbs> findByTitleContainingIgnoreCaseAndBbsGroupIdx(String title, Long bbsGroupIdx, Pageable pageable);
}
