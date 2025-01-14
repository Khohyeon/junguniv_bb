package com.example.junguniv_bb.domain.uznBranch.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "UZN_BRANCH_SUB")
@Data
public class UznBranchSub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BRANCH_SUB_IDX", nullable = false)
    private Integer branchSubIdx;

    @Column(name = "SUB_NAME", length = 100, nullable = true)
    private String subName;

    @Column(name = "SORTNO", nullable = true)
    private Integer sortno;

    @Column(name = "CHK_USE", length = 1, nullable = true)
    private String chkUse;

    @Column(name = "URL", nullable = true)
    private String url;

    @ManyToOne
    @JoinColumn(name = "BRANCH_IDX", nullable = false)
    private UznBranch branch;
}
