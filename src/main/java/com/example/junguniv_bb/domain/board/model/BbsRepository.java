package com.example.junguniv_bb.domain.board.model;

import aj.org.objectweb.asm.commons.Remapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BbsRepository extends JpaRepository<Bbs, Long>, JpaSpecificationExecutor<Bbs> {
    Page<Bbs> findByTitleContainingIgnoreCaseAndBbsGroup(String title, BbsGroup bbsGroup, Pageable pageable);

    /**
     * Detail 페이지 접근 시 조회 수 1증가 시키는 쿼리
     */
    @Modifying
    @Query("UPDATE Bbs b SET b.readNum = b.readNum + 1 WHERE b.bbsIdx = :bbsIdx")
    void incrementReadNum(@Param("bbsIdx") Long bbsIdx);

}
