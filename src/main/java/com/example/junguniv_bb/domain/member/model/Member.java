package com.example.junguniv_bb.domain.member.model;

import com.example.junguniv_bb._core.common.BaseTime;
import com.example.junguniv_bb.domain.member._enum.UserType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

/**
 * 회원 테이블
 */
@Entity
@Table(name = "MEMBER")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_IDX", nullable = false)
    private Long memberIdx; // 회원 IDX

    @Column(name = "RESIDENT_NUMBER", length = 100)
    private String residentNumber; // 주민등록번호

    @Column(name = "NAME", length = 100)
    private String name; // 이름

    @Column(name = "PWD", length = 100)
    private String pwd; // 비밀번호

    @Column(name = "CHINESE_NAME", length = 100)
    private String chineseName; // 한자 이름

    @Column(name = "CORPORATION_NAME", length = 100)
    private String corporationName; // 지점명

    @Column(name = "CORPORATION_CODE", length = 100)
    private String corporationCode; // 지점코드

    @Column(name = "ENG_NAME", length = 100)
    private String engName; // 영문 이름

    @Column(name = "FINAL_EDUCATION_TYPE", length = 200)
    private String finalEducationType; // 최종학력

    @Column(name = "FINAL_EDUCATION_NAME", length = 200)
    private String finalEducationName; // 최종학력 학교명

    @Column(name = "TEL_MOBILE", length = 100)
    private String telMobile; // 핸드폰

    @Column(name = "TEL_HOME", length = 100)
    private String telHome; // 자택전화

    @Column(name = "BANK_NAME", length = 100)
    private String bankName; // 환불은행

    @Column(name = "BANK_NUMBER", length = 100)
    private String bankNumber; // 환불계좌번호

    @Column(name = "ZIPCODE", length = 20)
    private String zipcode; // 우편번호

    @Column(name = "ADDR1", length = 255)
    private String addr1; // 주소1

    @Column(name = "ADDR2", length = 255)
    private String addr2; // 주소2

    @Column(name = "ENG_ADDR1", length = 100)
    private String engAddr1; // 영문 주소

    @Column(name = "EMAIL", length = 100)
    private String email; // 이메일

    @Column(name = "BANK_CODE", length = 100)
    private String bankCode; // 계좌코드

    @Column(name = "BANK_USER_NAME", length = 100)
    private String bankUserName; // 예금주

    @Column(name = "USER_ID", length = 100)
    private String userId; // 아이디

    @Column(name = "PERMISSION_DATE", length = 10)
    private String permissionDate; // 허가일

    @Column(name = "TEMPORARY_NUMBER", length = 100)
    private String temporaryNumber; // 사번/관리번호

    @Column(name = "CHK_MILITARY", length = 1)
    private String chkMilitary; // 군필여부

    @Column(name = "SCHOOL_NUMBER", length = 100)
    private String schoolNumber; // 학번

    @Column(name = "DEGREE", length = 100)
    private String degree; // 학위

    @Column(name = "MAJOR", length = 100)
    private String major; // 전공

    @Column(name = "CHK_FOREIGNER", length = 1)
    private String chkForeigner; // 외국인 여부

    @Column(name = "SEX", length = 1)
    private String sex; // 성별

    @Column(name = "MEMBER_BELONGTO_TYPE", length = 100)
    private String memberBelongtoType; // 개인/단체 이름

    @Column(name = "INTRODUCE", columnDefinition = "MEDIUMTEXT")
    private String introduce; // 취업희망사항, 자기소개

    @Column(name = "JOIN_ROUTE", length = 100)
    private String joinRoute; // 가입경로

    @Column(name = "CHK_MAIL_RECEIVE", length = 1)
    private String chkMailReceive; // 메일수신 여부

    @Column(name = "FNAME_PICTURE", length = 200)
    private String fnamePicture; // 증명사진

    @Column(name = "BIRTHDAY", length = 10)
    private String birthday; // 생년월일

    @Column(name = "MANAGER_USERID", length = 100)
    private String managerUserid; // 관리자 아이디

    @Column(name = "MANAGER_NOTE", columnDefinition = "MEDIUMTEXT")
    private String managerNote; // 담당관리자 메모

    @Column(name = "ACADEMIC_ABILITY", columnDefinition = "MEDIUMTEXT")
    private String academicAbility; // 학력 및 전공

    @Column(name = "CAREER", columnDefinition = "MEDIUMTEXT")
    private String career; // 이전 직장 업무내용

    @Column(name = "BOOKS", columnDefinition = "TEXT")
    private String books; // 저서

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_TYPE", length = 100)
    private UserType userType; // 회원구분

    @Column(name = "CHANGE_NOTE", columnDefinition = "MEDIUMTEXT")
    private String changeNote; // 변경사유

    @Column(name = "TEACHER_TYPE", length = 100)
    private String teacherType; // 교사 타입

    @Column(name = "JOB_ZIPCODE", length = 20)
    private String jobZipcode; // 직장 우편번호

    @Column(name = "JOB_ADDR1", length = 100)
    private String jobAddr1; // 직장 주소1

    @Column(name = "JOB_ADDR2", length = 100)
    private String jobAddr2; // 직장 주소2

    @Column(name = "TEL_FAX", length = 100)
    private String telFax; // 팩스번호

    @Column(name = "MEMBER_STATE", length = 1)
    private String memberState; // 계정중지 여부

    @Column(name = "JOB_CEO", length = 20)
    private String jobCeo; // 대표자명

    @Column(name = "JOB_NUMBER", length = 100)
    private String jobNumber; // 사업자번호

    @Column(name = "JOB_TEL_FAX", length = 100)
    private String jobTelFax; // 직장 팩스번호

    @Column(name = "JOB_EVENT", length = 100)
    private String jobEvent; // 업종

    @Column(name = "JOB_CONDITION", length = 100)
    private String jobCondition; // 업태

    @Column(name = "JOB_REGION", length = 100)
    private String jobRegion; // 직장 지역

    @Column(name = "JOB_ETC", columnDefinition = "MEDIUMTEXT")
    private String jobEtc; // 직장 기타 내용

    @Column(name = "JOB_DEPT", length = 100)
    private String jobDept; // 부서

    @Column(name = "JOB_POSITION", length = 100)
    private String jobPosition; // 직위

    @Column(name = "JOB_DUTY", length = 100)
    private String jobDuty; // 담당업무

    @Column(name = "JOB_SCALE", length = 100)
    private String jobScale; // 기업규모

    @Column(name = "JOB_CHK_LISTED", length = 1)
    private String jobChkListed; // 상장 여부

    @Column(name = "JOB_STAFF_NUMBER", length = 100)
    private String jobStaffNumber; // 직원 수

    @Column(name = "JOB_EMPLOYEE_TYPE", length = 100)
    private String jobEmployeeType; // 근무형태

    @Column(name = "JOB_NAME", length = 100)
    private String jobName; // 위탁기업명

    @Column(name = "JOB_WORK_STATE", length = 100)
    private String jobWorkState; // 근무상태

    @Column(name = "RESIGNATION_REGISTDATE", length = 10)
    private String resignationRegistdate; // 퇴사일자

    @Column(name = "RESIGNATION_CONTENTS", length = 100)
    private String resignationContents; // 퇴사사유

    @Column(name = "JOB_JOIN_DATE", length = 10)
    private String jobJoinDate; // 입사일자

    @Column(name = "JOB_TEL_OFFICE", length = 100)
    private String jobTelOffice; // 직장 전화번호

    @Column(name = "JOB_EMAIL", length = 100)
    private String jobEmail; // 직장 이메일

    @Column(name = "JOB_TOTAL_SALES", length = 100)
    private String jobTotalSales; // 매출액

    @Column(name = "PWD_ENC", length = 100)
    private String pwdEnc; // 비밀번호 암호화

    @Column(name = "REAL_NAME", length = 100)
    private String realName; // 실명

    @Column(name = "REAL_DATE", length = 10)
    private String realDate; // 인증일

    @Column(name = "CHK_REAL", length = 1)
    private String chkReal; // 실명인증 여부

    @Column(name = "LOGIN_ALLOW_IP", length = 100)
    private String loginAllowIp; // 허용 IP

    @Column(name = "LOGIN_DENY_IP", length = 100)
    private String loginDenyIp; // 차단 IP

    @Column(name = "LOGIN_DATE")
    private LocalDateTime loginDate; // 최신 로그인 시간(마지막 로그인)

    @Column(name = "LOGIN_CLIENT_IP", length = 100)
    private String loginClientIp; // 마지막 IP

    @Column(name = "LOGIN_MEMBER_COUNT")
    private Long loginMemberCount; // 동시접속자

    @Column(name = "MEMBER_JOB_IDX")
    private Long memberJobIdx; // 소속회사idx

    @Column(name = "MASTER_ID", length = 100)
    private String masterId; // 관리자 아이디

    @Column(name = "CHARGER_ID", length = 100)
    private String chargerId; // 계약관리자idx

    @Column(name = "CORPORATION_IDX")
    private Long corporationIdx; // 지점idx

    @Column(name = "AUTH_LEVEL")
    private Long authLevel; // 권한레벨

    @Column(name = "BIRTHDAY_TYPE", length = 1)
    private String birthdayType; // 생일 양력/음력 구분

    @Column(name = "LICENSE", columnDefinition = "MEDIUMTEXT")
    private String license; // 취득자격증

    @Column(name = "APPLY_DATE")
    private LocalDateTime applyDate; // 적용일

    @Column(name = "APPLY_USER_ID", length = 100)
    private String applyUserId; // 적용한 아이디

    @Column(name = "APPLY_CLIENT_IP", length = 100)
    private String applyClientIp; // 적용한 IP

    @Column(name = "STUDY_LEVEL", length = 100)
    private String studyLevel; // 충성도

    @Column(name = "STUDY_ETC", length = 100)
    private String studyEtc; // 메모

    @Column(name = "JOB_HOMEPAGE", length = 100)
    private String jobHomepage; // 직장 홈페이지 이름

    @Column(name = "JOIN_INTEREST", length = 100)
    private String joinInterest; // 관심분야

    @Column(name = "EPKI_USERDN", length = 100)
    private String epkiUserdn; // 공인인증서코드

    @Column(name = "EPKI_REGIST_DATE")
    private LocalDateTime epkiRegistDate; // 공인인증서인증일

    @Column(name = "EPKI_CLIENT_IP", length = 45)
    private String epkiClientIp; // 공인인증서인증ip

    @Column(name = "JOB_INSURANCE_NUMBER", length = 200)
    private String jobInsuranceNumber; // 고용보험(관리번호)

    @Column(name = "RESIDENT_7", length = 50)
    private String resident7; // 주민등록번호 뒷자리

    @Column(name = "JUMIN", length = 10)
    private String jumin; // 주민등록번호

    @Column(name = "CONTRACTOR_IDX")
    private Long contractorIdx; // 계약관리자 idx

    @Column(name = "CONTRACTOR_NAME", length = 100)
    private String contractorName; // 계약관리자명

    @Column(name = "CONTRACTOR_TEL", length = 100)
    private String contractorTel; // 계약관리자 - 휴대전화

    @Column(name = "CONTRACTOR_ETC", length = 100)
    private String contractorEtc; // 계약관리자 - 기타

    @Column(name = "EPKI_KEY", columnDefinition = "MEDIUMTEXT")
    private String epkiKey; // 공인인증서키

    @Column(name = "AGE", length = 10)
    private String age; // 연령

    @Column(name = "CAREER_CATE1", length = 100)
    private String careerCate1; // 취업희망직종 중분류

    @Column(name = "CAREER_CATE2", length = 100)
    private String careerCate2; // 취업희망직종 소분류

    @Column(name = "CAREER_CATE3", length = 100)
    private String careerCate3; // 취업희망직종 세분류

    @Column(name = "CAREER_YEAR", length = 100)
    private String careerYear; // 희망분야 경력 년

    @Column(name = "CAREER_MONTH", length = 100)
    private String careerMonth; // 희망분야 경력 월

    @Column(name = "CAREER_SKILL", length = 100)
    private String careerSkill; // 보유스킬

    @Column(name = "CAREER_ETC", length = 100)
    private String careerEtc; // 기타사항

    @Column(name = "NICE_DUP_INFO", columnDefinition = "MEDIUMTEXT")
    private String niceDupInfo; // 인증코드

    @Column(name = "PWD_CNT")
    private Long pwdCnt; // 비밀번호변경횟수

    @Column(name = "NW_INO", length = 50)
    private String nwIno; // 비용수급사업장 번호

    @Column(name = "TRNEE_SE", length = 10)
    private String trneeSe; // 훈련생구분

    @Column(name = "IRGLBR_SE", length = 10)
    private String irglbrSe; // 비정규직구분

    @Column(name = "HOMEPAGE_JOIN", length = 1)
    private String homepageJoin; // 홈페이지 가입

    @Column(name = "NICE_REG_DATE")
    private LocalDateTime niceRegDate; // 나이스 약관동의일

    @Column(name = "LOGIN_FAIL_CNT")
    private Long loginFailCnt; // 로그인 실패 횟수

    @Column(name = "PWD_DATE")
    private LocalDateTime pwdDate; // 비밀번호 변경일

    @Column(name = "CHK_TEMP_PWD", length = 1)
    private String chkTempPwd; // 임시비밀번호 체크

    @Column(name = "REFUND_CARD_CATE1", length = 100)
    private String refundCardCate1; // 카드발급분야 대분류

    @Column(name = "REFUND_CARD_CATE2", length = 100)
    private String refundCardCate2; // 카드발급분야 중분류

    @Column(name = "FNAME2", length = 250)
    private String fname2; // 첨부파일2

    @Column(name = "FNAME2_NAME", length = 250)
    private String fname2Name; // 첨부파일명2

    @Column(name = "FNAME3", length = 250)
    private String fname3; // 첨부파일3

    @Column(name = "FNAME3_NAME", length = 250)
    private String fname3Name; // 첨부파일명3

    @Column(name = "FNAME4", length = 250)
    private String fname4; // 첨부파일4

    @Column(name = "FNAME4_NAME", length = 250)
    private String fname4Name; // 첨부파일명4

    @Lob
    @Column(name = "AGREE_PAGE", columnDefinition = "LONGTEXT")
    private String agreePage; // 회원가입약관내용

    @Column(name = "AGREE_DATE")
    private LocalDateTime agreeDate; // 약관동의일

    @Column(name = "LOGIN_FAIL_TIME")
    private LocalDateTime loginFailTime; // 로그인 실패 시간

    @Column(name = "PWD_OLD", length = 100)
    private String pwdOld; // 예전 비밀번호

    @Column(name = "LOGIN_PASS", length = 1, columnDefinition = "char(1) default 'N'")
    private String loginPass; // 로그인제한 여부

    @Column(name = "FNAME_SAUP", length = 100)
    private String fnameSaup; // 사업자등록증 사본

    @Column(name = "CHK_SMS_RECEIVE", length = 1)
    private String chkSmsReceive; // SMS 수신 여부

    @Column(name = "CHK_DORMANT", length = 1)
    private String chkDormant; // 휴면 상태여부

    @Column(name = "CHK_IDENTITY_VERIFICATION", length = 1)
    private String chkIdentityVerification; // 본인인증 예외 처리 여부

    @Column(name = "CHK_PWD_CHANGE", length = 1)
    private String chkPwdChange; // 비밀번호 변경 예외 처리 여부

    @Column(name = "JOB_COURSE_DUTY", length = 10)
    private String jobCourseDuty; // 환급/일반 과정 담당

    @Column(name = "COMPANY_TYPE", length = 10)
    private String companyType; // 기업 구분(본사/지사)

    @Column(name = "COMPANY_HOMEPAGE_USE", length = 1)
    private String companyHomepageUse; // 기업 홈페이지 사용 여부

    @Column(name = "COMPANY_URL", length = 100)
    private String companyUrl; // 기업 홈페이지 URL

    @Column(name = "FNAME_LOGO", length = 255)
    private String fnameLogo; // 로고 파일명

    @Column(name = "FNAME_LOGO_NAME", length = 100)
    private String fnameLogoName; // 로고 파일명 원본

    @Column(name = "COUNSEL_NUMBER", length = 100)
    private String counselNumber; // 상담번호

    @Column(name = "COUNSEL_TIME", length = 100)
    private String counselTime; // 상담시간

    @Column(name = "MAIN_IMG", length = 255)
    private String mainImg; // 메인 상단 이미지명

    @Column(name = "MAIN_IMG_NAME", length = 100)
    private String mainImgName; // 메인 상단 이미지명 원본
    
    @Column(name = "SUB_IMG", length = 255)
    private String subImg; // 서브 상단 이미지명

    @Column(name = "SUB_IMG_NAME", length = 100)
    private String subImgName; // 서브 상단 이미지명 원본
}
