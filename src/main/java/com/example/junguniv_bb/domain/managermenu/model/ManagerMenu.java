package com.example.junguniv_bb.domain.managermenu.model;

import com.example.junguniv_bb._core.common.BaseTime;
import com.example.junguniv_bb.domain.managermenu._enum.MenuType;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "MANAGER_MENU")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerMenu extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_IDX")
    private Long menuIdx; // 메뉴IDX

    @Column(name = "MENU_NAME", nullable = false)
    private String menuName; // 메뉴명

    @Column(name = "SORTNO")
    private Integer sortno; // 정렬값

    @Column(name = "CHK_USE", length = 1)
    private String chkUse; // 사용여부

    @Column(name = "URL")
    private String url; // URL

    @Enumerated(EnumType.STRING)
    @Column(name = "MENU_GROUP", length = 200)
    private MenuType menuGroup; // 메뉴그룹

    @Column(name = "MENU_HELP", length = 255)
    private String menuHelp; // 명칭

    @Column(name = "CHK_PERSON", length = 1)
    private String chkPerson; // 개인정보유무

    @Column(name = "PERSON_INFOR", length = 255)
    private String personInfor; // 개인정보항목

    @JsonBackReference // 250123 순환참조 방지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_IDX") // 부모 메뉴
    private ManagerMenu parent;

    @JsonManagedReference // 250123 순환참조 방지
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sortno ASC") // 정렬 기준
    private List<ManagerMenu> children = new ArrayList<>();

    @Column(name = "MENU_LEVEL")
    private Long menuLevel; // 메뉴레벨(1,2,3차)
}