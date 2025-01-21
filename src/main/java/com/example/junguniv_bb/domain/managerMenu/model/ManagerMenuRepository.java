package com.example.junguniv_bb.domain.managerMenu.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Meta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ManagerMenuRepository extends JpaRepository<ManagerMenu, Long> {

    // 최상위 메뉴 조회
    List<ManagerMenu> findByParentIsNullAndChkUseOrderBySortno(String chkUse);

    List<ManagerMenu> findByParent_MenuIdxAndChkUseOrderBySortno(Long parentMenuIdx, String y);

    @Meta(comment = "메뉴명 검색")
    @Query("SELECT m FROM ManagerMenu m WHERE upper(m.menuName) LIKE upper(CONCAT('%', :menuName, '%'))")
    Page<ManagerMenu> findByMenuNameContainingIgnoreCase(@Param("menuName") String menuName, Pageable pageable);

    @Meta(comment = "메뉴 상세 조회 (N+1 방지)")
    @Query("SELECT DISTINCT m FROM ManagerMenu m " +
           "LEFT JOIN FETCH m.parent " +
           "LEFT JOIN FETCH m.children " +
           "WHERE m.menuIdx = :id")
    Optional<ManagerMenu> findByIdWithParentAndChildren(@Param("id") Long id);

    @Meta(comment = "메뉴 목록 검색 (N+1 방지)")
    @Query("SELECT DISTINCT m FROM ManagerMenu m " +
           "LEFT JOIN FETCH m.parent " +
           "LEFT JOIN FETCH m.children " +
           "WHERE upper(m.menuName) LIKE upper(CONCAT('%', :menuName, '%'))")
    Page<ManagerMenu> findByMenuNameContainingIgnoreCaseWithParentAndChildren(
            @Param("menuName") String menuName, Pageable pageable);

    @Meta(comment = "메뉴 목록 전체 조회 (N+1 방지)")
    @Query("SELECT DISTINCT m FROM ManagerMenu m " +
           "LEFT JOIN FETCH m.parent " +
           "LEFT JOIN FETCH m.children")
    Page<ManagerMenu> findAllWithParentAndChildren(Pageable pageable);
}