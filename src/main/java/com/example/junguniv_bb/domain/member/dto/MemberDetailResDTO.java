package com.example.junguniv_bb.domain.member.dto;

import com.example.junguniv_bb.domain.member.model.Member;

import com.example.junguniv_bb.domain.member._enum.UserType;
import java.time.LocalDateTime;

public record MemberDetailResDTO(
    Long memberIdx, // 회원 IDX
    String residentNumber, // 주민등록번호
    String name, // 이름
    String chineseName, // 한자 이름
    String corporationName, // 지점명
    String corporationCode, // 지점코드
    String engName, // 영문 이름
    String finalEducationType, // 최종학력
    String finalEducationName, // 최종학력명
    String telMobile, // 휴대폰번호
    String telHome, // 집전화번호
    String bankName, // 은행명
    String bankNumber, // 계좌번호
    String zipcode, // 우편번호
    String addr1, // 주소
    String addr2, // 상세주소
    String engAddr1, // 영문 주소
    String email, // 이메일
    String bankCode, // 은행코드
    String bankUserName, // 계좌소유자명
    String userId, // 아이디
    String permissionDate, // 권한일자
    String temporaryNumber, // 임시번호
    String chkMilitary, // 군복무여부
    String schoolNumber, // 학교번호
    String degree, // 학위
    String major, // 전공
    String chkForeigner, // 외국인여부
    String sex, // 성별
    String memberBelongtoType, // 회원소속유형
    String introduce, // 소개
    String joinRoute, // 가입경로
    String chkMailReceive, // 메일수신여부
    String fnamePicture, // 프로필사진
    String birthday, // 생년월일
    String managerUserid, // 관리자아이디
    String managerNote, // 관리자메모
    String academicAbility, // 학력
    String career, // 경력
    String books, // 책
    UserType userType, // 사용자유형
    String changeNote, // 변경내용
    String teacherType, // 교강사유형
    String jobZipcode, // 직장우편번호
    String jobAddr1, // 직장주소
    String jobAddr2, // 직장상세주소
    String telFax, // 직장전화번호
    String memberState, // 회원상태
    String jobCeo, // 직장대표자
    String jobNumber, // 직장사업자번호
    String jobTelFax, // 직장전화번호
    String jobEvent, // 직장이벤트
    String jobCondition, // 직장조건
    String jobRegion, // 직장지역
    String jobEtc, // 직장기타
    String jobDept, // 직장부서
    String jobPosition, // 직장직책
    String jobDuty, // 직장직무
    String jobScale, // 직장규모
    String jobChkListed, // 직장체크리스트
    String jobStaffNumber, // 직장직원수
    String jobEmployeeType, // 직장직원유형 
    String jobName, // 직장이름
    String jobWorkState, // 직장근무상태
    String resignationRegistdate, // 퇴사등록일자
    String resignationContents, // 퇴사내용
    String jobJoinDate, // 직장입사일자
    String jobTelOffice, // 직장전화번호
    String jobEmail, // 직장이메일
    String jobTotalSales, // 직장총매출
    String pwdEnc, // 비밀번호암호화
    String realName, // 실명
    String realDate, // 실명일자
    String chkReal, // 실명체크
    String loginAllowIp, // 로그인허용IP
    String loginDenyIp, // 로그인거부IP
    LocalDateTime loginDate, // 로그인일자
    String loginClientIp, // 로그인클라이언트IP
    Long loginMemberCount, // 로그인회원수
    Long memberJobIdx, // 회원직장IDX
    String masterId, // 마스터아이디
    String chargerId, // 채널아이디
    Long corporationIdx, // 지점IDX
    Long authLevel, // 권한레벨
    String birthdayType, // 생년월일유형
    String license, // 자격증
    LocalDateTime applyDate, // 신청일자
    String applyUserId, // 신청아이디
    String applyClientIp, // 신청클라이언트IP
    String studyLevel, // 학습레벨
    String studyEtc, // 학습기타
    String jobHomepage, // 직장홈페이지
    String joinInterest, // 가입관심분야
    String epkiUserdn, // 회원E-PKI사용자ID
    LocalDateTime epkiRegistDate, // 회원E-PKI등록일자
    String epkiClientIp, // 회원E-PKI클라이언트IP
    String jobInsuranceNumber, // 직장보험번호
    String resident7, // 주민7자
    String jumin, // 주민번호
    Long contractorIdx, // 계약자IDX
    String contractorName, // 계약자명
    String contractorTel, // 계약자전화번호
    String contractorEtc, // 계약자기타
    String epkiKey, // 회원E-PKI키
    String age, // 나이
    String careerCate1, // 경력분야1
    String careerCate2, // 경력분야2
    String careerCate3, // 경력분야3
    String careerYear, // 경력년수
    String careerMonth, // 경력월수
    String careerSkill, // 경력기술
    String careerEtc, // 경력기타
    String niceDupInfo, // 좋아요중복정보
    Long pwdCnt, // 비밀번호카운트
    String nwIno, // 네이버아이디
    String trneeSe, // 트레니오서비스
    String irglbrSe, // 이력서서비스
    String homepageJoin, // 홈페이지가입
    LocalDateTime niceRegDate, // 좋아요등록일자
    Long loginFailCnt, // 로그인실패횟수
    LocalDateTime pwdDate, // 비밀번호일자
    String chkTempPwd, // 임시비밀번호체크
    String refundCardCate1, // 환불카드분야1
    String refundCardCate2, // 환불카드분야2
    String fname2, // 프로필사진2
    String fname2Name, // 프로필사진2명
    String fname3, // 프로필사진3
    String fname3Name, // 프로필사진3명
    String fname4, // 프로필사진4
    String fname4Name, // 프로필사진4명
    String agreePage, // 동의페이지
    LocalDateTime agreeDate, // 동의일자
    LocalDateTime loginFailTime, // 로그인실패시간
    String pwdOld, // 이전비밀번호
    String loginPass, // 로그인패스
    String fnameSaup, // 프로필사진사용
    String chkSmsReceive, // SMS 수신 여부
    String chkDormant, // 휴면 상태여부
    String chkIdentityVerification, // 본인인증 예외 처리 여부
    String chkPwdChange, // 비밀번호 변경 예외 처리 여부
    String jobCourseDuty // 환급/일반 과정 담당
) {
    public static MemberDetailResDTO from(Member member) {
        return new MemberDetailResDTO(
            member.getMemberIdx(),
            member.getResidentNumber(),
            member.getName(),
            member.getChineseName(),
            member.getCorporationName(),
            member.getCorporationCode(),
            member.getEngName(),
            member.getFinalEducationType(),
            member.getFinalEducationName(),
            member.getTelMobile(),
            member.getTelHome(),
            member.getBankName(),
            member.getBankNumber(),
            member.getZipcode(),
            member.getAddr1(),
            member.getAddr2(),
            member.getEngAddr1(),
            member.getEmail(),
            member.getBankCode(),
            member.getBankUserName(),
            member.getUserId(),
            member.getPermissionDate(),
            member.getTemporaryNumber(),
            member.getChkMilitary(),
            member.getSchoolNumber(),
            member.getDegree(),
            member.getMajor(),
            member.getChkForeigner(),
            member.getSex(),
            member.getMemberBelongtoType(),
            member.getIntroduce(),
            member.getJoinRoute(),
            member.getChkMailReceive(),
            member.getFnamePicture(),
            member.getBirthday(),
            member.getManagerUserid(),
            member.getManagerNote(),
            member.getAcademicAbility(),
            member.getCareer(),
            member.getBooks(),
            member.getUserType(),
            member.getChangeNote(),
            member.getTeacherType(),
            member.getJobZipcode(),
            member.getJobAddr1(),
            member.getJobAddr2(),
            member.getTelFax(),
            member.getMemberState(),
            member.getJobCeo(),
            member.getJobNumber(),
            member.getJobTelFax(),
            member.getJobEvent(),
            member.getJobCondition(),
            member.getJobRegion(),
            member.getJobEtc(),
            member.getJobDept(),
            member.getJobPosition(),
            member.getJobDuty(),
            member.getJobScale(),
            member.getJobChkListed(),
            member.getJobStaffNumber(),
            member.getJobEmployeeType(),
            member.getJobName(),
            member.getJobWorkState(),
            member.getResignationRegistdate(),
            member.getResignationContents(),
            member.getJobJoinDate(),
            member.getJobTelOffice(),
            member.getJobEmail(),
            member.getJobTotalSales(),
            member.getPwdEnc(),
            member.getRealName(),
            member.getRealDate(),
            member.getChkReal(),
            member.getLoginAllowIp(),
            member.getLoginDenyIp(),
            member.getLoginDate(),
            member.getLoginClientIp(),
            member.getLoginMemberCount(),
            member.getMemberJobIdx(),
            member.getMasterId(),
            member.getChargerId(),
            member.getCorporationIdx(),
            member.getAuthLevel(),
            member.getBirthdayType(),
            member.getLicense(),
            member.getApplyDate(),
            member.getApplyUserId(),
            member.getApplyClientIp(),
            member.getStudyLevel(),
            member.getStudyEtc(),
            member.getJobHomepage(),
            member.getJoinInterest(),
            member.getEpkiUserdn(),
            member.getEpkiRegistDate(),
            member.getEpkiClientIp(),
            member.getJobInsuranceNumber(),
            member.getResident7(),
            member.getJumin(),
            member.getContractorIdx(),
            member.getContractorName(),
            member.getContractorTel(),
            member.getContractorEtc(),
            member.getEpkiKey(),
            member.getAge(),
            member.getCareerCate1(),
            member.getCareerCate2(),
            member.getCareerCate3(),
            member.getCareerYear(),
            member.getCareerMonth(),
            member.getCareerSkill(),
            member.getCareerEtc(),
            member.getNiceDupInfo(),
            member.getPwdCnt(),
            member.getNwIno(),
            member.getTrneeSe(),
            member.getIrglbrSe(),
            member.getHomepageJoin(),
            member.getNiceRegDate(),
            member.getLoginFailCnt(),
            member.getPwdDate(),
            member.getChkTempPwd(),
            member.getRefundCardCate1(),
            member.getRefundCardCate2(),
            member.getFname2(),
            member.getFname2Name(),
            member.getFname3(),
            member.getFname3Name(),
            member.getFname4(),
            member.getFname4Name(),
            member.getAgreePage(),
            member.getAgreeDate(),
            member.getLoginFailTime(),
            member.getPwdOld(),
            member.getLoginPass(),
            member.getFnameSaup(),
            member.getChkSmsReceive(),
            member.getChkDormant(),
            member.getChkIdentityVerification(),
            member.getChkPwdChange(),
            member.getJobCourseDuty()
        );
    }
}
