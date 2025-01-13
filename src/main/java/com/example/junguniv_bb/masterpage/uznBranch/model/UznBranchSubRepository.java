package com.example.junguniv_bb.masterpage.uznBranch.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UznBranchSubRepository extends JpaRepository<UznBranchSub, Long> {
    List<UznBranchSub> findAllByBranch_BranchIdxAndChkUseOrderBySortno(Integer branchIdx, String chkUse);
}
