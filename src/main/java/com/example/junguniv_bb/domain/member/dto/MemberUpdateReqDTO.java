package com.example.junguniv_bb.domain.member.dto;

import com.example.junguniv_bb.domain.member.model.Member;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import com.example.junguniv_bb._core.exception.ValidExceptionMessage;
import com.example.junguniv_bb.domain.member._enum.UserType;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

public record MemberUpdateReqDTO(
    String residentNumber, // 주민등록번호
    String name, // 이름

    String pwd, // 비밀번호 (변경시에만 입력)
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
    String fnameLogoName, // 로고 파일명
    String counselNumber, // 상담번호
    String counselTime, // 상담시간
    String mainImg, // 메인 상단 이미지
    String mainImgName, // 메인 상단 이미지명
    String subImg, // 서브 상단 이미지
    String subImgName, // 서브 상단 이미지명
    String fnameSaupName, // 사업자등록증 사본 원본 파일명
    MultipartFile mainImgFile, // 메인 이미지 파일(임시)
    MultipartFile subImgFile, // 서브 이미지 파일(임시)
    MultipartFile fnameLogoFile, // 로고 이미지 파일(임시)
    MultipartFile fnameSaupFile // 사업자등록증 사본 파일(임시)
) {
    public void updateEntity(Member member, String encodedPwd) {
        if (residentNumber != null) member.setResidentNumber(residentNumber);
        if (name != null) member.setName(name);
        if (pwd != null) member.setPwd(encodedPwd);
        if (chineseName != null) member.setChineseName(chineseName);
        if (corporationName != null) member.setCorporationName(corporationName);
        if (corporationCode != null) member.setCorporationCode(corporationCode);
        if (engName != null) member.setEngName(engName);
        if (finalEducationType != null) member.setFinalEducationType(finalEducationType);
        if (finalEducationName != null) member.setFinalEducationName(finalEducationName);
        if (telMobile != null) member.setTelMobile(telMobile);
        if (telHome != null) member.setTelHome(telHome);
        if (bankName != null) member.setBankName(bankName);
        if (bankNumber != null) member.setBankNumber(bankNumber);
        if (zipcode != null) member.setZipcode(zipcode);
        if (addr1 != null) member.setAddr1(addr1);
        if (addr2 != null) member.setAddr2(addr2);
        if (engAddr1 != null) member.setEngAddr1(engAddr1);
        if (email != null) member.setEmail(email);
        if (bankCode != null) member.setBankCode(bankCode);
        if (bankUserName != null) member.setBankUserName(bankUserName);
        if (userId != null) member.setUserId(userId);
        if (permissionDate != null) member.setPermissionDate(permissionDate);
        if (temporaryNumber != null) member.setTemporaryNumber(temporaryNumber);
        if (chkMilitary != null) member.setChkMilitary(chkMilitary);
        if (schoolNumber != null) member.setSchoolNumber(schoolNumber);
        if (degree != null) member.setDegree(degree);
        if (major != null) member.setMajor(major);
        if (chkForeigner != null) member.setChkForeigner(chkForeigner);
        if (sex != null) member.setSex(sex);
        if (memberBelongtoType != null) member.setMemberBelongtoType(memberBelongtoType);
        if (introduce != null) member.setIntroduce(introduce);
        if (joinRoute != null) member.setJoinRoute(joinRoute);
        if (chkMailReceive != null) member.setChkMailReceive(chkMailReceive);
        if (fnamePicture != null) member.setFnamePicture(fnamePicture);
        if (birthday != null) member.setBirthday(birthday);
        if (managerUserid != null) member.setManagerUserid(managerUserid);
        if (managerNote != null) member.setManagerNote(managerNote);
        if (academicAbility != null) member.setAcademicAbility(academicAbility);
        if (career != null) member.setCareer(career);
        if (books != null) member.setBooks(books);
        if (userType != null) member.setUserType(userType);
        if (changeNote != null) member.setChangeNote(changeNote);
        if (teacherType != null) member.setTeacherType(teacherType);
        if (jobZipcode != null) member.setJobZipcode(jobZipcode);
        if (jobAddr1 != null) member.setJobAddr1(jobAddr1);
        if (jobAddr2 != null) member.setJobAddr2(jobAddr2);
        if (telFax != null) member.setTelFax(telFax);
        if (memberState != null) member.setMemberState(memberState);
        if (jobCeo != null) member.setJobCeo(jobCeo);
        if (jobNumber != null) member.setJobNumber(jobNumber);
        if (jobTelFax != null) member.setJobTelFax(jobTelFax);
        if (jobEvent != null) member.setJobEvent(jobEvent);
        if (jobCondition != null) member.setJobCondition(jobCondition);
        if (jobRegion != null) member.setJobRegion(jobRegion);
        if (jobEtc != null) member.setJobEtc(jobEtc);
        if (jobDept != null) member.setJobDept(jobDept);
        if (jobPosition != null) member.setJobPosition(jobPosition);
        if (jobDuty != null) member.setJobDuty(jobDuty);
        if (jobScale != null) member.setJobScale(jobScale);
        if (jobChkListed != null) member.setJobChkListed(jobChkListed);
        if (jobStaffNumber != null) member.setJobStaffNumber(jobStaffNumber);
        if (jobEmployeeType != null) member.setJobEmployeeType(jobEmployeeType);
        if (jobName != null) member.setJobName(jobName);
        if (jobWorkState != null) member.setJobWorkState(jobWorkState);
        if (resignationRegistdate != null) member.setResignationRegistdate(resignationRegistdate);
        if (resignationContents != null) member.setResignationContents(resignationContents);
        if (jobJoinDate != null) member.setJobJoinDate(jobJoinDate);
        if (jobTelOffice != null) member.setJobTelOffice(jobTelOffice);
        if (jobEmail != null) member.setJobEmail(jobEmail);
        if (jobTotalSales != null) member.setJobTotalSales(jobTotalSales);
        if (pwdEnc != null) member.setPwdEnc(pwdEnc);
        if (realName != null) member.setRealName(realName);
        if (realDate != null) member.setRealDate(realDate);
        if (chkReal != null) member.setChkReal(chkReal);
        if (loginAllowIp != null) member.setLoginAllowIp(loginAllowIp);
        if (loginDenyIp != null) member.setLoginDenyIp(loginDenyIp);
        if (loginDate != null) member.setLoginDate(loginDate);
        if (loginClientIp != null) member.setLoginClientIp(loginClientIp);
        if (loginMemberCount != null) member.setLoginMemberCount(loginMemberCount);
        if (memberJobIdx != null) member.setMemberJobIdx(memberJobIdx);
        if (masterId != null) member.setMasterId(masterId);
        if (chargerId != null) member.setChargerId(chargerId);
        if (corporationIdx != null) member.setCorporationIdx(corporationIdx);
        if (authLevel != null) member.setAuthLevel(authLevel);
        if (birthdayType != null) member.setBirthdayType(birthdayType);
        if (license != null) member.setLicense(license);
        if (applyDate != null) member.setApplyDate(applyDate);
        if (applyUserId != null) member.setApplyUserId(applyUserId);
        if (applyClientIp != null) member.setApplyClientIp(applyClientIp);
        if (studyLevel != null) member.setStudyLevel(studyLevel);
        if (studyEtc != null) member.setStudyEtc(studyEtc);
        if (jobHomepage != null) member.setJobHomepage(jobHomepage);
        if (joinInterest != null) member.setJoinInterest(joinInterest);
        if (epkiUserdn != null) member.setEpkiUserdn(epkiUserdn);
        if (epkiRegistDate != null) member.setEpkiRegistDate(epkiRegistDate);
        if (epkiClientIp != null) member.setEpkiClientIp(epkiClientIp);
        if (jobInsuranceNumber != null) member.setJobInsuranceNumber(jobInsuranceNumber);
        if (resident7 != null) member.setResident7(resident7);
        if (jumin != null) member.setJumin(jumin);
        if (contractorIdx != null) member.setContractorIdx(contractorIdx);
        if (contractorName != null) member.setContractorName(contractorName);
        if (contractorTel != null) member.setContractorTel(contractorTel);
        if (contractorEtc != null) member.setContractorEtc(contractorEtc);
        if (epkiKey != null) member.setEpkiKey(epkiKey);
        if (age != null) member.setAge(age);
        if (careerCate1 != null) member.setCareerCate1(careerCate1);
        if (careerCate2 != null) member.setCareerCate2(careerCate2);
        if (careerCate3 != null) member.setCareerCate3(careerCate3);
        if (careerYear != null) member.setCareerYear(careerYear);
        if (careerMonth != null) member.setCareerMonth(careerMonth);
        if (careerSkill != null) member.setCareerSkill(careerSkill);
        if (careerEtc != null) member.setCareerEtc(careerEtc);
        if (niceDupInfo != null) member.setNiceDupInfo(niceDupInfo);
        if (pwdCnt != null) member.setPwdCnt(pwdCnt);
        if (nwIno != null) member.setNwIno(nwIno);
        if (trneeSe != null) member.setTrneeSe(trneeSe);
        if (irglbrSe != null) member.setIrglbrSe(irglbrSe);
        if (homepageJoin != null) member.setHomepageJoin(homepageJoin);
        if (niceRegDate != null) member.setNiceRegDate(niceRegDate);
        if (loginFailCnt != null) member.setLoginFailCnt(loginFailCnt);
        if (pwdDate != null) member.setPwdDate(pwdDate);
        if (chkTempPwd != null) member.setChkTempPwd(chkTempPwd);
        if (refundCardCate1 != null) member.setRefundCardCate1(refundCardCate1);
        if (refundCardCate2 != null) member.setRefundCardCate2(refundCardCate2);
        if (fname2 != null) member.setFname2(fname2);
        if (fname2Name != null) member.setFname2Name(fname2Name);
        if (fname3 != null) member.setFname3(fname3);
        if (fname3Name != null) member.setFname3Name(fname3Name);
        if (fname4 != null) member.setFname4(fname4);
        if (fname4Name != null) member.setFname4Name(fname4Name);
        if (agreePage != null) member.setAgreePage(agreePage);
        if (agreeDate != null) member.setAgreeDate(agreeDate);
        if (loginFailTime != null) member.setLoginFailTime(loginFailTime);
        if (pwdOld != null) member.setPwdOld(pwdOld);
        if (loginPass != null) member.setLoginPass(loginPass);
        if (fnameSaup != null) member.setFnameSaup(fnameSaup);
        if (chkSmsReceive != null) member.setChkSmsReceive(chkSmsReceive);
        if (chkDormant != null) member.setChkDormant(chkDormant);
        if (chkIdentityVerification != null) member.setChkIdentityVerification(chkIdentityVerification);
        if (chkPwdChange != null) member.setChkPwdChange(chkPwdChange);
        if (jobCourseDuty != null) member.setJobCourseDuty(jobCourseDuty);
        if (companyType != null) member.setCompanyType(companyType);
        if (companyHomepageUse != null) member.setCompanyHomepageUse(companyHomepageUse);
        if (companyUrl != null) member.setCompanyUrl(companyUrl);
        if (fnameLogo != null) member.setFnameLogo(fnameLogo);
        if (fnameLogoName != null) member.setFnameLogoName(fnameLogoName);
        if (counselNumber != null) member.setCounselNumber(counselNumber);
        if (counselTime != null) member.setCounselTime(counselTime);
        if (mainImg != null) member.setMainImg(mainImg);
        if (mainImgName != null) member.setMainImgName(mainImgName);
        if (subImg != null) member.setSubImg(subImg);
        if (subImgName != null) member.setSubImgName(subImgName);
        if (fnameSaupName != null) member.setFnameSaupName(fnameSaupName);
    }
}


