package com.example.junguniv_bb.domain.board.model;

import com.example.junguniv_bb._core.common.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BBS_FILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BbsFile extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_IDX")
    private Integer fileIdx; // 첨부파일 IDX

    @Column(name = "BBSID", nullable = false, length = 255)
    private String bbsId; // 게시판 코드

    @JoinColumn(name = "BBS_IDX", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Bbs bbs; // 게시글 IDX (FK)

    @Column(name = "FNAME1", length = 255)
    private String fName1; // 첨부파일명

    @Column(name = "FNAME1_USERFILE", length = 255)
    private String fName1UserFile; // 첨부파일명 (사용자 업로드)

    @Column(name = "WRITE_USERID", length = 255)
    private String writeUserId; // 최초 등록자 ID

    @Column(name = "MODIFY_USERID", length = 255)
    private String modifyUserId; // 최종 수정자 ID

}