package com.example.junguniv_bb.domain.board.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BbsCommentRepository extends JpaRepository<BbsComment, Long> {
    List<BbsComment> findAllByBbsIdx(Bbs bbs);
}
