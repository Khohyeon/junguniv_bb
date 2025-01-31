package com.example.junguniv_bb.domain.refundPrice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "REFUND_PRICE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_price_idx")
    private Long refundPriceIdx; // 코드 (기본키)

    @Column(name = "refund_price_type", length = 255)
    private String refundPriceType; // 지원금 유형

    @Column(name = "refund_price_name", length = 100)
    private String refundPriceName; // 지원금 종류명

    @Column(name = "discount_type")
    private String discountType; // 할인 금액

    @Column(name = "refund_rate")
    private Long refundRate; // 할인/지원율

    @Column(name = "chk_use", length = 1)
    private String chkUse; // 사용 여부

    @Column(name = "sortno")
    private Long sortno; // 정렬값
}