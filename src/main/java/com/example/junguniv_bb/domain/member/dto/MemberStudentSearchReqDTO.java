package com.example.junguniv_bb.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberStudentSearchReqDTO {
    private String name;                    // 이름
    private String userId;                  // 아이디
    private String birthYear;               // 생년
    private String birthMonth;              // 생월
    private String birthDay;                // 생일
    private String telMobile;               // 휴대폰
    private String email;                   // 이메일
    private String chkDormant;              // 휴면계정 여부 (Y/N)
    private String loginPass;               // 로그인 제한 여부 (Y/N)
    private String chkForeigner;            // 외국인 여부 (Y/N)
    private String sex;                     // 성별 (M/F)
    private String jobName;                 // 근무회사
    private String jobWorkState;            // 근무상태
    private String jobDept;                 // 근무부서
    private String chkSmsReceive;           // 마케팅 수신(문자) (Y/N)
    private String chkMailReceive;          // 마케팅 수신(이메일) (Y/N)
    private String chkIdentityVerification; // 인증예외 (Y/N)
    private String loginClientIp;           // 로그인ID(IP)

    // 생년월일 조합 메서드
    public String getBirthday() {
        if (birthYear == null || birthYear.isEmpty() || 
            birthMonth == null || birthMonth.isEmpty() || 
            birthDay == null || birthDay.isEmpty()) {
            return null;
        }
        try {
            int year = Integer.parseInt(birthYear);
            int month = Integer.parseInt(birthMonth);
            int day = Integer.parseInt(birthDay);
            
            return String.format("%d-%02d-%02d", year, month, day);
        } catch (NumberFormatException e) {
            return null;
        }
    }
} 