package com.example.junguniv_bb.domain.refundPrice.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundPriceRepository extends JpaRepository<RefundPrice, Long> {

    Page<RefundPrice> findByStudyTypeAndRefundPriceNameContainingIgnoreCase(String studyType, String refundPriceName, Pageable pageable);

    Page<RefundPrice> findByStudyTypeAndRefundPriceNameContainingIgnoreCaseAndRefundPriceType(String studyType, String refundPriceName, String refundPriceType, Pageable pageable);

    Page<RefundPrice> findByRefundPriceNameContainingIgnoreCase(String refundPriceName, Pageable pageable);

    Page<RefundPrice> findByRefundPriceNameContainingIgnoreCaseAndRefundPriceType(String refundPriceName, String refundPriceType, Pageable pageable);
}
