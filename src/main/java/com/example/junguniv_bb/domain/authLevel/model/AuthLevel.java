package com.example.junguniv_bb.domain.authLevel.model;

import com.example.junguniv_bb._core.common.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "AUTH_LEVEL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthLevel extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTH_LEVEL_IDX", nullable = false)
    private Long authLevelIdx; // 권한 IDX

    @Column(name = "AUTH_LEVEL_NAME", length = 200)
    private String authLevelName; // 권한 레벨명

    @Column(name = "AUTH_LEVEL")
    private Long authLevel; // 권한 레벨

    @Column(name = "CHK_VIEW_JUMIN", length = 1)
    private String chkViewJumin; // 주민번호 출력 여부
}
