package com.example.junguniv_bb.domain.board.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BbsFileRepository extends JpaRepository<BbsFile, Long> {
    List<BbsFile> findAllByBbs(Bbs bbs);
}
