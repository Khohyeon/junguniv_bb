package com.example.junguniv_bb.domain.college.model;

import com.example.junguniv_bb._core.common.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "COLLEGE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class College extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COLLEGE_IDX")
    private Long collegeIdx;  // 분류 선택 코드 IDX (기본키)

    @Column(name = "COLLEGE_NAME", length = 255)
    private String collegeName;  // 분류 선택 이름

    @Column(name = "ENG_NAME", length = 255)
    private String engName;  // 분류 선택 영어 이름

    @Column(name = "CHK_USE", length = 1)
    private String chkUse;  // 사용 여부 (Y/N)

    @Column(name = "COLLEGE_CODE", length = 100)
    private String collegeCode;  // 분류 선택 코드

    @Column(name = "CONTENTS", columnDefinition = "MEDIUMTEXT")
    private String contents;  // 내용
}
