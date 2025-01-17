package com.example.junguniv_bb.domain.board.model;

import com.example.junguniv_bb._core.common.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BBS_GROUP")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BbsGroup extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BBS_GROUP_IDX")
    private Long bbsGroupIdx; // 게시판 IDX

    @Column(name = "BBSID", length = 255)
    private String bbsId; // 게시판 코드

    @Column(name = "BBS_GROUP_NAME", length = 255)
    private String bbsGroupName; // 게시판명

    @Column(name = "SKIN", length = 255)
    private String skin; // 스킨 종류

    @Column(name = "CATEGORY", length = 255)
    private String category; // 카테고리

    @Column(name = "FILENUM")
    private Integer fileNum; // 첨부파일 수

    @Column(name = "READAUTH", length = 255)
    private String readAuth; // 읽기 권한

    @Column(name = "WRITEAUTH", length = 255)
    private String writeAuth; // 쓰기 권한

    @Column(name = "REPLYAUTH", length = 255)
    private String replyAuth; // 답변 권한

    @Column(name = "COMMENTAUTH", length = 255)
    private String commentAuth; // 댓글 권한

    @Column(name = "OPTION_SECRETAUTH", length = 1)
    private String optionSecretAuth; // 옵션 비밀 권한 (Y, N)

    @Column(name = "OPTION_REPLYAUTH", length = 1)
    private String optionReplyAuth; // 옵션 답변 권한 (Y, N)

    @Column(name = "OPTION_COMMENTAUTH", length = 1)
    private String optionCommentAuth; // 옵션 댓글 권한 (Y, N)

    @Column(name = "FILEAUTH", length = 255)
    private String fileAuth; // 파일 첨부 권한

    public BbsGroup(Long bbsGroupIdx, String bbsGroupName, String bbsId) {
        this.bbsGroupIdx = bbsGroupIdx;
        this.bbsGroupName = bbsGroupName;
        this.bbsId = bbsId;
    }
}
