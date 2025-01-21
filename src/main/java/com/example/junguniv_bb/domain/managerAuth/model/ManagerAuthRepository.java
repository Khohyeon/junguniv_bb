package com.example.junguniv_bb.domain.managerAuth.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Meta;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ManagerAuthRepository extends JpaRepository<ManagerAuth, Long> {


    @Meta(comment = "메뉴 삭제시 같은 menuIdx를 가진 모든 ManagerAuth 삭제")
    @Modifying
    @Transactional
    void deleteAllByMenuIdx(Long menuIdx);

    @Meta(comment = "관리자 권한 삭제시 같은 authLevel을 가진 모든 ManagerAuth 삭제")
    @Modifying
    @Transactional
    void deleteAllByAuthLevel(Long authLevel);

    @Meta(comment = "권한레벨이 Update할때 같은 권한레벨을 가진 엔티티의 권한레벨을 변경하는 메서드")
    @Modifying
    @Transactional
    @Query("UPDATE ManagerAuth m SET m.authLevel = :newAuthLevel WHERE m.authLevel = :oldAuthLevel")
    void updateAuthLevelForAuthLevel(@Param("oldAuthLevel") Long oldAuthLevel,
            @Param("newAuthLevel") Long newAuthLevel);

    @Meta(comment = "권한 레벨로 메뉴권한레벨 목록 조회")
    List<ManagerAuth> findAllByAuthLevel(Long authLevel);

    @Meta(comment = "메뉴 인덱스와 권한 레벨로 검색")
    @Query("SELECT m FROM ManagerAuth m WHERE m.menuIdx = :menuIdx AND m.authLevel = :authLevel")
    ManagerAuth findByMenuIdxAndAuthLevel(@Param("menuIdx") Long menuIdx, @Param("authLevel") Long authLevel);

    @Meta(comment = "권한 레벨로 검색")
    Page<ManagerAuth> findByAuthLevel(Long authLevel, Pageable pageable);

    @Meta(comment = "메뉴명으로 ManagerAuth 검색 (JPQL)")
    @Query("SELECT ma FROM ManagerAuth ma, ManagerMenu mm WHERE ma.menuIdx = mm.menuIdx AND mm.menuName = :menuName")
    Page<ManagerAuth> findAllByMenuName(@Param("menuName") String menuName, Pageable pageable);
}
