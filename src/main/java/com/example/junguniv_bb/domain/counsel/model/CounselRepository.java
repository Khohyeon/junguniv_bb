package com.example.junguniv_bb.domain.counsel.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounselRepository extends JpaRepository<Counsel, Long> {
    Page<Counsel> findByCounselNameContainingIgnoreCase(String counselName, Pageable pageable);
    Page<Counsel> findByCounselNameContainingIgnoreCaseAndCounselState(String counselName, Integer counselState, Pageable pageable);
}
