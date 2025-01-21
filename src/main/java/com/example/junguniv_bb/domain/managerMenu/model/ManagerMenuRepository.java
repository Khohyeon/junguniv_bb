package com.example.junguniv_bb.domain.managerMenu.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagerMenuRepository extends JpaRepository<ManagerMenu, Integer> {

    // 최상위 메뉴 조회
    List<ManagerMenu> findByParentIsNullAndChkUseOrderBySortno(String chkUse);


    List<ManagerMenu> findByParent_MenuIdxAndChkUseOrderBySortno(Integer parentMenuIdx, String y);
}