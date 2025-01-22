package com.example.junguniv_bb.domain.popup.model;

import com.example.junguniv_bb._core.common.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "POPUP")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Popup extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POPUP_IDX")
    private Long popupIdx; // 팝업 idx

    @Column(name = "POPUP_NAME", length = 255)
    private String popupName; // 팝업 이름

    @Column(name = "START_DATE", length = 10)
    private String startDate; // 시작 날짜

    @Column(name = "END_DATE", length = 10)
    private String endDate; // 종료 날짜

    @Column(name = "WIDTH_SIZE", length = 100)
    private String widthSize; // 팝업 가로 너비

    @Column(name = "HEIGHT_SIZE", length = 100)
    private String heightSize; // 팝업 세로 너비

    @Column(name = "TOP_SIZE", length = 100)
    private String topSize; // 팝업 위치 (TOP)

    @Column(name = "LEFT_SIZE", length = 100)
    private String leftSize; // 팝업 위치 (LEFT)

    @Lob
    @Column(name = "CONTENTS")
    private String contents; // 팝업 내용

    @Column(name = "CHK_TODAY", length = 1)
    private String chkToday; // 오늘 하루만 표시 여부

    @Column(name = "POPUP_TYPE", length = 100)
    private String popupType; // 팝업 종류 (popup: 팝업창, layer: 레이어, poplayer: 팝업 레이어)

    @Column(name = "CHK_OPEN", length = 1)
    private String chkOpen; // 중지 여부

    @Column(name = "CHK_SCROLLBAR", length = 1)
    private String chkScrollbar; // 스크롤바 표시 여부

    @Column(name = "POPUP_URL")
    private String popupUrl; // 팝업 URL

    @Column(name = "BGCOLOR")
    private String bgcolor; // 팝업 배경색

    // 첨부파일1
    @Column(name = "FNAME1", length = 200)
    private String fname1;

    // 비공개파일명1
    @Column(name = "FNAME1_NAME", length = 100)
    private String fname1Name;


}