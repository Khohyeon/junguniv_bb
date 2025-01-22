package com.example.junguniv_bb.domain.authlevel.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Meta;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthLevelRepository extends JpaRepository<AuthLevel, Long> {


    /* 변경하려는 authLevel 값이 기존의 다른 엔티티에 존재하는지 확인하는 메서드 */
    boolean existsByAuthLevelAndAuthLevelIdxNot(Long authLevel, Long authLevelIdx);

    /* 같은 권한레벨이 있는지 확인하는 메서드 */
    boolean existsByAuthLevel(Long authLevel);

    // authLevelName은(컬럼명이 authlevel_name 이어서 JPA 쿼리 메서드 사용불가. 따라서 JPQL로 작성)
    @Meta(comment = "권한명 검색")
    @Query("SELECT a FROM AuthLevel a WHERE upper(a.authLevelName) LIKE upper(CONCAT('%', :authLevelName, '%')) ESCAPE '!'")
    Page<AuthLevel> findByAuthLevelNameContainingIgnoreCase(String authLevelName, Pageable pageable);

    @Meta(comment = "권한 레벨로 AuthLevel을 찾습니다.")
    Optional<AuthLevel> findByAuthLevel(Long authLevel);
}
