package com.example.junguniv_bb.domain.counsel.dto;

import com.example.junguniv_bb.domain.counsel.model.Counsel;

public record CounselUpdateReqDTO(
        Long counselIdx,
        String counselName,
        String name,
        String talkTime,
        String degreeHope,
        String addr1,
        String addr2,
        String zipcode,
        String telMobile,
        String email,
        String finalEducationType,
        String pwd,
        String memo,
        String license,
        String fname1,
        String fname2,
        String fname3,
        String fname1Name,
        String fname2Name,
        String fname3Name,
        String answerMemo,
        String counselState
) {

    public Counsel updateEntity() {
        return new Counsel(
                counselIdx, counselName, name, telMobile, talkTime, memo, answerMemo, null, null ,addr1, addr2, zipcode, finalEducationType,
                fname1, license, fname2, fname3, null, fname1Name, fname2Name, fname3Name, pwd, degreeHope, email, counselState);
    }
}
