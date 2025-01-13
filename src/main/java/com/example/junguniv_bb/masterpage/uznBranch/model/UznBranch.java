package com.example.junguniv_bb.masterpage.uznBranch.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "UZN_BRANCH")
@Data
public class UznBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BRANCH_IDX", nullable = false)
    private Integer branchIdx;

    @Column(name = "BRANCH_NAME", length = 100, nullable = true)
    private String branchName;

    @Column(name = "SORTNO", nullable = true)
    private Integer sortno;

    @Column(name = "CHK_USE", length = 1, nullable = true)
    private String chkUse;


}
