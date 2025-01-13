package com.example.junguniv_bb.masterpage.unzMenu.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "UZN_MENU")
@Data
@NoArgsConstructor
public class UznMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_IDX")
    private Integer menuIdx;

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