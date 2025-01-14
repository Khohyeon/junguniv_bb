package com.example.junguniv_bb.domain.uznMenu.model;

import com.example.junguniv_bb._core.common.BaseTime;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "UZN_MENU")
@Getter
@Setter
@NoArgsConstructor
public class UznMenu extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_IDX")
    private Long menuIdx;

    @Column(name = "MENU_NAME", nullable = false)
    private String menuName;

    @Column(name = "SORTNO")
    private Integer sortno;

    @Column(name = "CHK_USE", length = 1)
    private String chkUse;

    @Column(name = "URL")
    private String url;

    @ManyToOne
    @JoinColumn(name = "PARENT_IDX") // 부모 메뉴
    private UznMenu parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sortno ASC") // 정렬 기준
    private List<UznMenu> children = new ArrayList<>();
}