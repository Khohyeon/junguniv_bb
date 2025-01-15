package com.example.junguniv_bb.domain.board.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
@Table(name = "BBS")
public class Bbs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BBS_IDX", nullable = false)
    private Long bbsIdx;

    @Column(name = "BBS_GROUP_IDX", nullable = false)
    private Long bbsGroupIdx;

    @Column(name = "BBSID", nullable = false, length = 255)
    private String bbsId;

    @Column(name = "CATEGORY", length = 255)
    private String category;

    @Column(name = "TITLE", length = 255)
    private String title;

    @Lob
    @Column(name = "CONTENTS")
    private String contents;

    @Column(name = "URL", length = 255)
    private String url;

    @Column(name = "WRITER", length = 255)
    private String writer;

    @Column(name = "PWD", length = 255)
    private String pwd;

    @Column(name = "IP", length = 255)
    private String ip;

    @Column(name = "READNUM")
    private Long readNum;

    @Column(name = "CHK_MAIN", length = 1)
    private String chkMain; // y/n

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "CHK_SECRET", length = 1)
    private String chkSecret; // y/n

    @Column(name = "CHK_HIDDEN", length = 1)
    private String chkHidden; // y/n

    @Column(name = "CHK_AUTH", length = 255)
    private String chkAuth;

    @Column(name = "REPLY_BBS_IDX")
    private Long replyBbsIdx;

    @Column(name = "REPLY_SORTNO")
    private Long replySortNo;

    @Column(name = "WRITE_USERID", length = 255)
    private String writeUserId;

    @Column(name = "MODIFY_USERID", length = 255)
    private String modifyUserId;

    @Column(name = "REPLY_DEPTH")
    private Long replyDepth;

    @Column(name = "C_START_DATE", length = 255)
    private String cStartDate;

    @Column(name = "C_END_DATE", length = 255)
    private String cEndDate;

}
