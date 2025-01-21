package com.example.junguniv_bb.domain.managerMenu.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Meta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagerMenuRepository extends JpaRepository<ManagerMenu, Integer> {

    // 최상위 메뉴 조회
    List<ManagerMenu> findByParentIsNullAndChkUseOrderBySortno(String chkUse);

    List<ManagerMenu> findByParent_MenuIdxAndChkUseOrderBySortno(Integer parentMenuIdx, String y);

    // menuName은(컬럼명이 menuname 이어서 JPA 쿼리 메서드 사용불가. 따라서 JPQL로 작성)
    @Meta(comment = "메뉴명 검색")
    @Query("SELECT m FROM ManagerMenu m WHERE upper(m.menuName) LIKE upper(CONCAT('%', :menuName, '%'))")
    Page<ManagerMenu> findByMenuNameContainingIgnoreCase(@Param("menuName") String menuName, Pageable pageable);
}