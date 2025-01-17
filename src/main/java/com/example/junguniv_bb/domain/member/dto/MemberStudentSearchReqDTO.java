package com.example.junguniv_bb.domain.member.dto;


public record MemberStudentSearchReqDTO (
    String name,                    // 이름
    String userId,                  // 아이디
    String birthYear,               // 생년
    String birthMonth,              // 생월
    String birthDay,                // 생일
    String telMobile,               // 휴대폰
    String email,                   // 이메일
    String chkDormant,              // 휴면계정 여부 (Y/N)
    String loginPass,               // 로그인 제한 여부 (Y/N)
    String chkForeigner,            // 외국인 여부 (Y/N)
    String sex,                     // 성별 (M/F)
    String jobName,                 // 근무회사
    String jobWorkState,            // 근무상태
    String jobDept,                // 근무부서
    String chkSmsReceive,           // 마케팅 수신(문자) (Y/N)
    String chkMailReceive,          // 마케팅 수신(이메일) (Y/N)
    String chkIdentityVerification, // 인증예외 (Y/N)
    String loginClientIp           // 로그인ID(IP)
) {
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