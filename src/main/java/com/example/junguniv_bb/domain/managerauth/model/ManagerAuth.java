package com.example.junguniv_bb.domain.managerauth.model;

import com.example.junguniv_bb._core.common.BaseTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MANAGER_AUTH")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerAuth extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTH_IDX", nullable = false)
    private Long authIdx; // 메뉴권한레벨 IDX
    
    @Column(name = "MENU_IDX", nullable = false)
    private Long menuIdx; // 메뉴 IDX

    @Column(name = "AUTH_LEVEL", precision = 22, nullable = false)
    private Long authLevel; // 권한 레벨

    @Column(name = "MENU_READ_AUTH", precision = 22, nullable = true)
    private Long menuReadAuth; // 읽기 권한 레벨

    @Column(name = "MENU_WRITE_AUTH", precision = 22, nullable = true)
    private Long menuWriteAuth; // 등록 권한 레벨

    @Column(name = "MENU_MODIFY_AUTH", precision = 22, nullable = true)
    private Long menuModifyAuth; // 수정 권한 레벨

    @Column(name = "MENU_DELETE_AUTH", precision = 22, nullable = true)
    private Long menuDeleteAuth; // 삭제 권한 레벨
}
