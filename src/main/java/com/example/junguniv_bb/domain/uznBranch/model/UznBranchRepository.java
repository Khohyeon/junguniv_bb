package com.example.junguniv_bb.domain.uznBranch.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UznBranchRepository extends JpaRepository<UznBranch, Long> {
    List<UznBranch> findAllByChkUseOrderBySortno(String chkUse);
}
