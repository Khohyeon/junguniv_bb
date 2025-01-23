package com.example.junguniv_bb.domain.board.model;

import com.example.junguniv_bb._core.common.BaseTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
@Table(name = "BBS")
@NoArgsConstructor
public class Bbs extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BBS_IDX", nullable = false)
    private Long bbsIdx;

    @JoinColumn(name = "BBS_GROUP_IDX", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private BbsGroup bbsGroup;

    @Column(name = "BBSID", length = 255)
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

    @Column(name = "READ_NUM")
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

    @Column(name = "PARENT_BBS_IDX")
    private Long parentBbsIdx;

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

    @Column(name = "CHK_TOP_FIX", length = 1)
    private String chkTopFix;   // y/n

    @Column(name = "FIX_START_DATE")
    private LocalDate fixStartDate;

    @Column(name = "FIX_END_DATE")
    private LocalDate fixEndDate;

    @Column(name = "RECIPIENT_NAME")
    private String recipientName; // 받는 사람 이름

    @Column(name = "RECIPIENT_ID")
    private String recipientId; // 받는 사람 ID

    public Bbs(Long bbsIdx, String pwd, BbsGroup bbsGroupIdx, String bbsId, String title, String writer, String category, String chkTopFix, LocalDate fixStartDate, LocalDate fixEndDate,
               String chkMain, LocalDate startDate, LocalDate endDate, String contents, String recipientName, String recipientId, Long parentBbsIdx, Long readNum) {
        this.bbsIdx = bbsIdx;
        this.pwd = pwd;
        this.bbsGroup = bbsGroupIdx;
        this.bbsId = bbsId;
        this.title = title;
        this.writer = writer;
        this.writeUserId = writer;
        this.category = category;
        this.chkTopFix = chkTopFix;
        this.fixStartDate = fixStartDate;
        this.fixEndDate = fixEndDate;
        this.chkMain = chkMain;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contents = contents;
        this.recipientName = recipientName;
        this.recipientId = recipientId;
        this.parentBbsIdx = parentBbsIdx;
        this.readNum = readNum;
    }
}
