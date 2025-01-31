package com.example.junguniv_bb.domain.systemcode.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemCodeRepository extends JpaRepository<SystemCode, Long> {
    Page<SystemCode> findBySystemCodeNameContainingIgnoreCase(String systemCodeName, Pageable pageable);
}
