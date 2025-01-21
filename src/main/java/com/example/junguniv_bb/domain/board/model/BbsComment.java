package com.example.junguniv_bb.domain.board.model;

import com.example.junguniv_bb._core.common.BaseTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "BBS_COMMENT")
public class BbsComment extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_IDX", nullable = false)
    private Integer commentIdx; // 댓글 IDX

    @Column(name = "BBS_IDX", nullable = false)
    private Integer bbsIdx; // 게시글 IDX (FK)

    @Column(name = "WRITER", length = 255)
    private String writer; // 작성자 이름

    @Column(name = "PWD", length = 255)
    private String pwd; // 비밀번호

    @Column(name = "CONTENTS", length = 255)
    private String contents; // 내용

    @Column(name = "IP", length = 255)
    private String ip; // IP

    @Column(name = "CHK_SECRET", length = 255)
    private String chkSecret; // 비밀글 여부

    @Column(name = "WRITE_USERID", length = 255)
    private String writeUserId; // 최초 등록자 아이디

    @Column(name = "MODIFY_USERID", length = 255)
    private String modifyUserId; // 최종 수정자 아이디
}