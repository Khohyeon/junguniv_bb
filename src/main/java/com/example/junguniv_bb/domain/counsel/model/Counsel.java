package com.example.junguniv_bb.domain.counsel.model;

import com.example.junguniv_bb._core.common.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "COUNSEL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Counsel extends BaseTime {

    // 상담테이블 idx
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COUNSEL_IDX")
    private Long counselIdx;

    // 상담명
    @Column(name = "COUNSEL_NAME", length = 255)
    private String counselName;

    // 상담자명
    @Column(name = "NAME", length = 100)
    private String name;

    // 휴대폰
    @Column(name = "TEL_MOBILE", length = 100)
    private String telMobile;

    // 상담시간
    @Column(name = "TALK_TIME", length = 255)
    private String talkTime;

    // 상담신청내용
    @Lob
    @Column(name = "MEMO")
    private String memo;

    // 답변내용
    @Lob
    @Column(name = "ANSWER_MEMO")
    private String answerMemo;

    // 최종수정담당자 ID
    @Column(name = "APPLY_USER_ID", length = 100)
    private String applyUserId;

    // 최종수정 IP
    @Column(name = "APPLY_CLIENT_IP", length = 100)
    private String applyClientIp;

    // 주소
    @Column(name = "ADDR1", length = 255)
    private String addr1;

    // 상세주소
    @Column(name = "ADDR2", length = 255)
    private String addr2;

    // 집주소 코드
    @Column(name = "ZIPCODE", length = 100)
    private String zipcode;

    // 최종학력
    @Column(name = "FINAL_EDUCATION_TYPE", length = 255)
    private String finalEducationType;

    // 첨부파일1
    @Column(name = "FNAME1", length = 200)
    private String fname1;

    // 보유자격증
    @Lob
    @Column(name = "LICENSE")
    private String license;

    // 첨부파일2
    @Column(name = "FNAME2", length = 200)
    private String fname2;

    // 첨부파일3
    @Column(name = "FNAME3", length = 200)
    private String fname3;

    // 상담종류 코드
    @Column(name = "BBSID", length = 100)
    private String bbsid;

    // 비공개파일명1
    @Column(name = "FNAME1_NAME", length = 100)
    private String fname1Name;

    // 비공개파일명2
    @Column(name = "FNAME2_NAME", length = 100)
    private String fname2Name;

    // 비공개파일명3
    @Column(name = "FNAME3_NAME", length = 100)
    private String fname3Name;

    // 비밀번호
    @Column(name = "PWD", length = 100, nullable = false)
    private String pwd;

    // 희망과정
    @Column(name = "DEGREE_HOPE", length = 255, nullable = false)
    private String degreeHope;

    // 이메일
    @Column(name = "EMAIL", length = 255, nullable = false)
    private String email;

    // 상담상태
    @Column(name = "COUNSEL_STATE", length = 1, nullable = false)
    private String counselState;
}

