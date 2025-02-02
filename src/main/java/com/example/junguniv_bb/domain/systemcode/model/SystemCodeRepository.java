package com.example.junguniv_bb.domain.systemcode.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemCodeRepository extends JpaRepository<SystemCode, Long> {

    /* 시스템코드 검색 - 페이징 */
    Page<SystemCode> findBySystemCodeNameContainingIgnoreCase(@Param("systemCodeName") String systemCodeName, Pageable pageable);

    /* 시스템코드 검색 - 시스템코드명, 시스템코드그룹 */
    Optional<SystemCode> findBySystemCodeNameAndSystemCodeGroup(
        @Param("systemCodeName") String systemCodeName, 
        @Param("systemCodeGroup") String systemCodeGroup
    );

    /* 시스템코드 검색 - 시스템코드키, 시스템코드그룹 */
    Optional<SystemCode> findBySystemCodeKeyAndSystemCodeGroup(
        @Param("systemCodeKey") String systemCodeKey, 
        @Param("systemCodeGroup") String systemCodeGroup
    );
}
