package com.example.junguniv_bb.masterpage.unzMenu.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UznMenuRepository extends JpaRepository<UznMenu, Integer> {

    // 최상위 메뉴 조회
    List<UznMenu> findByParentIsNullAndChkUseOrderBySortno(String chkUse);

}