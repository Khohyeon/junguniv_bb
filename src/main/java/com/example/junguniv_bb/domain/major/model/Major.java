package com.example.junguniv_bb.domain.major.model;

import com.example.junguniv_bb._core.common.BaseTime;
import com.example.junguniv_bb.domain.college.model.College;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MAJOR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Major extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MAJOR_IDX")
    private Long majorIdx;  // 전공 IDX (기본키)

    @ManyToOne
    @JoinColumn(name = "COLLEGE_IDX", nullable = false)
    private College college;  // FK - 분류 선택 코드

    @Column(name = "MAJOR_NAME", length = 255)
    private String majorName;  // 전공 코드명

    @Column(name = "ENG_NAME", length = 255)
    private String engName;  // 전공 영어명

    @Column(name = "CHK_USE", length = 1)
    private String chkUse;  // 사용 여부 (Y/N)

    @Column(name = "PURPOSE", columnDefinition = "MEDIUMTEXT")
    private String purpose;  // 학습 목표

    @Column(name = "NOTE", columnDefinition = "MEDIUMTEXT")
    private String note;  // 비고 (추가 안내)

    @Column(name = "MAJOR_CODE", length = 100)
    private String majorCode;  // 전공 코드
}