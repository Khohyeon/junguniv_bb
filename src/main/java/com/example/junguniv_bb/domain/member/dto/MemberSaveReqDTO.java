package com.example.junguniv_bb.domain.member.dto;

import com.example.junguniv_bb.domain.member.model.Member;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import com.example.junguniv_bb._core.exception.ValidExceptionMessage;
import com.example.junguniv_bb.domain.member._enum.UserType;
import java.time.LocalDateTime;

public record MemberSaveReqDTO(
    String residentNumber, // 주민등록번호
    String name, // 이름

    @NotEmpty(message = ValidExceptionMessage.Message.INVALID_PASSWORD) @Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다.")
    String pwd, // 비밀번호
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
    
    @NotEmpty(message = ValidExceptionMessage.Message.INVALID_USERID) @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하로 입력하세요.")
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
    String jobCourseDuty, // 환급/일반 과정 담당
    String companyType, // 기업 구분(본사/지사)
    String companyHomepageUse, // 기업 홈페이지 사용 여부
    String companyUrl, // 기업 홈페이지 URL
    String fnameLogo, // 로고 파일
    String fnameLogoName, // 로고 원본 파일명
    String counselNumber, // 상담번호
    String counselTime, // 상담시간
    String mainImg, // 메인 상단 이미지
    String mainImgName, // 메인 상단 이미지 원본 파일명
    String subImg, // 서브 상단 이미지
    String subImgName, // 서브 상단 이미지 원본 파일명
    String fnameSaupName, // 사업자등록증 사본 원본 파일명
    MultipartFile mainImgFile, // 메인 이미지 파일(임시)
    MultipartFile subImgFile, // 서브 이미지 파일(임시)
    MultipartFile fnameLogoFile, // 로고 이미지 파일(임시)
    MultipartFile fnameSaupFile // 사업자등록증 사본 파일(임시)
) {
    public Member toEntity(String encodedPwd) {
        return Member.builder()
                .residentNumber(residentNumber)
                .name(name)
                .pwd(encodedPwd)
                .chineseName(chineseName)
                .corporationName(corporationName)
                .corporationCode(corporationCode)
                .engName(engName)
                .finalEducationType(finalEducationType)
                .finalEducationName(finalEducationName)
                .telMobile(telMobile)
                .telHome(telHome)
                .bankName(bankName)
                .bankNumber(bankNumber)
                .zipcode(zipcode)
                .addr1(addr1)
                .addr2(addr2)
                .engAddr1(engAddr1)
                .email(email)
                .bankCode(bankCode)
                .bankUserName(bankUserName)
                .userId(userId)
                .permissionDate(permissionDate)
                .temporaryNumber(temporaryNumber)
                .chkMilitary(chkMilitary)
                .schoolNumber(schoolNumber)
                .degree(degree)
                .major(major)
                .chkForeigner(chkForeigner)
                .sex(sex)
                .memberBelongtoType(memberBelongtoType)
                .introduce(introduce)
                .joinRoute(joinRoute)
                .chkMailReceive(chkMailReceive)
                .fnamePicture(fnamePicture)
                .birthday(birthday)
                .managerUserid(managerUserid)
                .managerNote(managerNote)
                .academicAbility(academicAbility)
                .career(career)
                .books(books)
                .userType(userType != null ? userType : UserType.ADMIN)
                .changeNote(changeNote)
                .teacherType(teacherType)
                .jobZipcode(jobZipcode)
                .jobAddr1(jobAddr1)
                .jobAddr2(jobAddr2)
                .telFax(telFax)
                .memberState(memberState != null ? memberState : "Y")
                .jobCeo(jobCeo)
                .jobNumber(jobNumber)
                .jobTelFax(jobTelFax)
                .jobEvent(jobEvent)
                .jobCondition(jobCondition)
                .jobRegion(jobRegion)
                .jobEtc(jobEtc)
                .jobDept(jobDept)
                .jobPosition(jobPosition)
                .jobDuty(jobDuty)
                .jobScale(jobScale)
                .jobChkListed(jobChkListed)
                .jobStaffNumber(jobStaffNumber)
                .jobEmployeeType(jobEmployeeType)
                .jobName(jobName)
                .jobWorkState(jobWorkState)
                .resignationRegistdate(resignationRegistdate)
                .resignationContents(resignationContents)
                .jobJoinDate(jobJoinDate)
                .jobTelOffice(jobTelOffice)
                .jobEmail(jobEmail)
                .jobTotalSales(jobTotalSales)
                .pwdEnc(pwdEnc)
                .realName(realName)
                .realDate(realDate)
                .chkReal(chkReal)
                .loginAllowIp(loginAllowIp)
                .loginDenyIp(loginDenyIp)
                .loginDate(loginDate)
                .loginClientIp(loginClientIp)
                .loginMemberCount(loginMemberCount)
                .memberJobIdx(memberJobIdx)
                .masterId(masterId)
                .chargerId(chargerId)
                .corporationIdx(corporationIdx)
                .authLevel(authLevel)
                .birthdayType(birthdayType)
                .license(license)
                .applyDate(applyDate)
                .applyUserId(applyUserId)
                .applyClientIp(applyClientIp)
                .studyLevel(studyLevel)
                .studyEtc(studyEtc)
                .jobHomepage(jobHomepage)
                .joinInterest(joinInterest)
                .epkiUserdn(epkiUserdn)
                .epkiRegistDate(epkiRegistDate)
                .epkiClientIp(epkiClientIp)
                .jobInsuranceNumber(jobInsuranceNumber)
                .resident7(resident7)
                .jumin(jumin)
                .contractorIdx(contractorIdx)
                .contractorName(contractorName)
                .contractorTel(contractorTel)
                .contractorEtc(contractorEtc)
                .epkiKey(epkiKey)
                .age(age)
                .careerCate1(careerCate1)
                .careerCate2(careerCate2)
                .careerCate3(careerCate3)
                .careerYear(careerYear)
                .careerMonth(careerMonth)
                .careerSkill(careerSkill)
                .careerEtc(careerEtc)
                .niceDupInfo(niceDupInfo)
                .pwdCnt(pwdCnt)
                .nwIno(nwIno)
                .trneeSe(trneeSe)
                .irglbrSe(irglbrSe)
                .homepageJoin(homepageJoin)
                .niceRegDate(niceRegDate)
                .loginFailCnt(loginFailCnt)
                .pwdDate(pwdDate)
                .chkTempPwd(chkTempPwd)
                .refundCardCate1(refundCardCate1)
                .refundCardCate2(refundCardCate2)
                .fname2(fname2)
                .fname2Name(fname2Name)
                .fname3(fname3)
                .fname3Name(fname3Name)
                .fname4(fname4)
                .fname4Name(fname4Name)
                .agreePage(agreePage)
                .agreeDate(agreeDate)
                .loginFailTime(loginFailTime)
                .pwdOld(pwdOld)
                .loginPass(loginPass)
                .fnameSaup(fnameSaup)
                .chkSmsReceive(chkSmsReceive)
                .chkDormant(chkDormant)
                .chkIdentityVerification(chkIdentityVerification)
                .chkPwdChange(chkPwdChange)
                .jobCourseDuty(jobCourseDuty)
                .companyType(companyType)
                .companyHomepageUse(companyHomepageUse)
                .companyUrl(companyUrl)
                .fnameLogo(fnameLogo)
                .fnameLogoName(fnameLogoName)
                .counselNumber(counselNumber)
                .counselTime(counselTime)
                .mainImg(mainImg)
                .mainImgName(mainImgName)
                .subImg(subImg)
                .subImgName(subImgName)
                .fnameSaupName(fnameSaupName)
                .build();
    }
}
