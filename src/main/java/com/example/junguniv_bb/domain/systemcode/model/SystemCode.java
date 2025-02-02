package com.example.junguniv_bb.domain.systemcode.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SYSTEM_CODE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SYSTEM_CODE_IDX")
    private Long systemCodeIdx; // 시스템코드 IDX

    @Column(name = "SYSTEM_CODE_NAME", length = 255)
    private String systemCodeName; // 코드명

    @Column(name = "SYSTEM_CODE_RULE", length = 255)
    private String systemCodeRule; // 코드규칙

    @Column(name = "SYSTEM_CODE_GROUP", length = 200)
    private String systemCodeGroup; // 코드 그룹

    @Column(name = "SYSTEM_CODE_KEY", columnDefinition = "MEDIUMTEXT")
    private String systemCodeKey; // 코드 키

    @Column(name = "SYSTEM_CODE_VALUE", columnDefinition = "MEDIUMTEXT")
    private String systemCodeValue; // 코드값
}
