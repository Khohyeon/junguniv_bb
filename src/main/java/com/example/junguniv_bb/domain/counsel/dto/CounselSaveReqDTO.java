package com.example.junguniv_bb.domain.counsel.dto;

import com.example.junguniv_bb.domain.counsel.model.Counsel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

public record CounselSaveReqDTO(
        @NotBlank(message = "상담 이름은 필수 입력 값입니다.")
        String counselName,
        @NotBlank(message = "이름은 필수 입력 값입니다.")
        String name,
        @NotBlank(message = "상담 시간을 입력해야 합니다.")
        String talkTime,
        @NotBlank(message = "희망 학위를 입력해야 합니다.")
        String degreeHope,
        @Pattern(regexp = "\\d{10,11}", message = "유효한 전화번호를 입력해야 합니다. \n(-) 제외하고 10~11자 입니다.)")
        String telMobile,
        @Email(message = "유효한 이메일 주소를 입력해야 합니다.")
        String email,
        @NotBlank(message = "최종 학력을 입력해야 합니다.")
        String finalEducationType,
        @Size(min = 4, max = 4, message = "비밀번호는 반드시 4자리여야 합니다.")
        String pwd,
        String memo,
        String license,
        String addr1,
        String addr2,
        String zipcode,
        String fname1,
        String fname2,
        String fname3,
        String fname1Name,
        String fname2Name,
        String fname3Name,
        String answerMemo,
        Integer counselState

) {

    public Counsel saveEntity() {
        return new Counsel(
                null, counselName, name, telMobile, talkTime, memo, answerMemo, null, null ,addr1, addr2, zipcode, finalEducationType,
                fname1, license, fname2, fname3, null, fname1Name, fname2Name, fname3Name, pwd, degreeHope, email,
                counselState == null ? 1: counselState);
    }
}
