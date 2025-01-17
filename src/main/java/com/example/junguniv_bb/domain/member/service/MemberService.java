package com.example.junguniv_bb.domain.member.service;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb._core.exception.ValidExceptionMessage;
import com.example.junguniv_bb._core.security.CustomUserDetails;
import com.example.junguniv_bb._core.util.FileUtils;
import com.example.junguniv_bb.domain.member._enum.UserType;
import com.example.junguniv_bb.domain.member.dto.*;
import com.example.junguniv_bb.domain.member.model.Member;
import com.example.junguniv_bb.domain.member.model.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    /* DI */
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /* 파일 업로드 디렉토리 경로 설정 */
    @Value("${file.upload.directories.member}")
    private String uploadDirPath;

    @Value("${file.upload.directories.member-temp}")
    private String tempDirPath;

    /* 프로젝트 실행시 yml 경로에 해당하는 파일 업로드 디렉토리 생성 */
    @PostConstruct
    public void init() {
        FileUtils.createDirectory(uploadDirPath);
        FileUtils.createDirectory(tempDirPath);
        cleanupTempDirectory(); // 임시 디렉토리 초기화
    }

    // 임시 폴더 청소 (24시간 이상 된 파일 삭제)
    @Scheduled(cron = "0 0 * * * *") // 매시간 실행
    public void cleanupTempDirectory() {
        FileUtils.cleanupDirectory(tempDirPath, 24); // 24시간 이상 된 파일 삭제
    }

    /**
     * 이미지 파일 업로드 처리
     * @param file MultipartFile
     * @return 저장된 파일명
     */
    private String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 이미지 파일 검증
        if (!FileUtils.isImageFile(file)) {
            throw new RuntimeException("이미지 파일만 업로드 가능합니다.");
        }

        // 파일 크기 검증 (10MB)
        if (!FileUtils.validateFileSize(file, 10 * 1024 * 1024)) {
            throw new RuntimeException("파일 크기는 10MB를 초과할 수 없습니다.");
        }

        try {
            // 파일 업로드 및 이동
            return FileUtils.uploadFile(file, tempDirPath);
        } catch (Exception e) {
            log.error("파일 저장 실패: {}", e.getMessage(), e);
            throw new RuntimeException("파일 저장에 실패했습니다: " + e.getMessage());
        }
    }

    /* 아이디 중복체크 */
    public boolean checkDuplicateId(String userId) {
        return memberRepository.findByUserId(userId).isPresent();
    }

    /* 페이징 */
    // TODO 검색 기능 및 권한 조건 추가 해야함.
    public ResponseEntity<?> memberPage(String referer, Pageable pageable) {
        
        // 페이지 조회
        Page<Member> memberPagePS;
        
        // Referer URL에서 userType 결정
        UserType userType = null;
        
        if (referer != null) {
            if (referer.contains("/student/")) {
                userType = UserType.STUDENT;
            } else if (referer.contains("/teacher/")) {
                userType = UserType.TEACHER;
            } else if (referer.contains("/company/")) {
                userType = UserType.COMPANY;
            } else if (referer.contains("/admin/")) {
                userType = UserType.ADMIN;
            }
        }

        // UserType에 따른 페이지 조회
        if (userType != null) {
            memberPagePS = memberRepository.findByUserType(userType, pageable);
        } else {
            memberPagePS = memberRepository.findAll(pageable);
        }
        
        // UserType에 따른 DTO 변환 및 반환
        if (userType != null) {
            switch (userType) {
                case STUDENT:
                    return ResponseEntity.ok(new MemberStudentPageResDTO(memberPagePS));
                case TEACHER:
                    return ResponseEntity.ok(new MemberTeacherPageResDTO(memberPagePS));
                case COMPANY:
                    return ResponseEntity.ok(new MemberCompanyPageResDTO(memberPagePS));
                case ADMIN:
                    return ResponseEntity.ok(new MemberAdminPageResDTO(memberPagePS));
                default:
                    return ResponseEntity.ok(new MemberPageResDTO(memberPagePS));
            }
        }
        
        // 기본 DTO 반환
        return ResponseEntity.ok(new MemberPageResDTO(memberPagePS));
    }

    /* 학생 검색 */
    public ResponseEntity<?> searchStudents(MemberStudentSearchReqDTO searchDTO, Pageable pageable) {
        // 검색 실행
        Page<Member> memberPagePS = memberRepository.searchStudents(
            searchDTO.name(),
            searchDTO.userId(),
            searchDTO.getBirthday(),
            searchDTO.telMobile(),
            searchDTO.email(),
            searchDTO.chkDormant(),
            searchDTO.loginPass(),
            searchDTO.chkForeigner(),
            searchDTO.sex(),
            searchDTO.jobName(),
            searchDTO.jobWorkState(),
            searchDTO.jobDept(),
            searchDTO.chkSmsReceive(),
            searchDTO.chkMailReceive(),
            searchDTO.chkIdentityVerification(),
            searchDTO.loginClientIp(),
            pageable
        );

        // 검색 결과를 DTO로 변환하여 반환
        return ResponseEntity.ok(new MemberStudentPageResDTO(memberPagePS));
    }

    /* 회원 이미지 파일 삭제 */
    private void deleteAllMemberFiles(Member member) {
        // 메인 이미지 삭제
        if (member.getMainImg() != null) {
            FileUtils.deleteFile(member.getMainImg(), uploadDirPath);
        }
        
        // 서브 이미지 삭제
        if (member.getSubImg() != null) {
            FileUtils.deleteFile(member.getSubImg(), uploadDirPath);
        }
        
        // 증명사진 삭제
        if (member.getFnamePicture() != null) {
            FileUtils.deleteFile(member.getFnamePicture(), uploadDirPath);
        }
        
        // 사업자등록증 삭제
        if (member.getFnameSaup() != null) {
            FileUtils.deleteFile(member.getFnameSaup(), uploadDirPath);
        }
        
        // 로고 삭제
        if (member.getFnameLogo() != null) {
            FileUtils.deleteFile(member.getFnameLogo(), uploadDirPath);
        }
        
        // 첨부파일2 삭제
        if (member.getFname2() != null) {
            FileUtils.deleteFile(member.getFname2(), uploadDirPath);
        }
        
        // 첨부파일3 삭제
        if (member.getFname3() != null) {
            FileUtils.deleteFile(member.getFname3(), uploadDirPath);
        }
        
        // 첨부파일4 삭제
        if (member.getFname4() != null) {
            FileUtils.deleteFile(member.getFname4(), uploadDirPath);
        }
    }

    /* 다중삭제 */
    @Transactional
    public void memberDeleteList(List<Long> idList) {
        
        // DB조회
        List<Member> memberListPS = memberRepository.findAllById(idList);

        if(memberListPS.size() != idList.size()) {
            throw new Exception400(ExceptionMessage.NOT_FOUND_MEMBER.getMessage());
        }

        // 각 회원의 이미지 파일들 삭제
        for (Member member : memberListPS) {
            deleteAllMemberFiles(member);
        }

        // 모두 삭제
        memberRepository.deleteAll(memberListPS);
    }

    /* 삭제 */
    @Transactional
    public void memberDelete(Long id) {

        // DB조회
        Member memberPS = memberRepository.findById(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_MEMBER.getMessage()));

        // 회원의 이미지 파일들 삭제
        deleteAllMemberFiles(memberPS);

        // 삭제
        memberRepository.delete(memberPS);
    }

    /* 수정 */
    @Transactional
    public void memberUpdate(Long id, MemberUpdateReqDTO reqDTO) {

        // DB조회
        Member memberPS = memberRepository.findById(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_MEMBER.getMessage()));

        // 비밀번호가 입력된 경우에만 암호화하여 업데이트
        String encodedPwd = null;
        if (reqDTO.pwd() != null && !reqDTO.pwd().isEmpty()) {
            // 비밀번호 유효성 검사
            if (reqDTO.pwd().length() < 4 || reqDTO.pwd().length() > 20) {
                throw new Exception400(ValidExceptionMessage.Message.INVALID_PASSWORD);
            }
            encodedPwd = passwordEncoder.encode(reqDTO.pwd());
        } else {
            encodedPwd = memberPS.getPwd();
        }

        // 트랜잭션 처리
        reqDTO.updateEntity(memberPS, encodedPwd);
    }

    /* 조회 */
    public MemberDetailResDTO memberDetail(Long id) {

        // DB조회
        Member memberPS = memberRepository.findById(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_MEMBER.getMessage()));

        // // CustomUserDetails에서 현재 메뉴 IDX와 권한 레벨을 가져옴
        // Long menuIdx = customUserDetails.getCurrentMenuIdx();
        // Long authLevelIdx = customUserDetails.getAuthLevel();

        // // 개인정보 접근 권한 확인
        // boolean hasPrivacyAccess = authLevelService.hasPrivacyAccessPermission(menuIdx, authLevelIdx);

        // if (!hasPrivacyAccess) {
        //     memberPS = authLevelService.maskMemberPrivateInfo(memberPS);
        // }

        // 개인정보(pwd 제외한) DTO 반환
        return MemberDetailResDTO.from(memberPS);
    }


    /* 등록 */
    @Transactional
    public void memberSave(MemberSaveReqDTO requestDTO) {
        String mainImgPath = null;
        String subImgPath = null;
        
        try {
            // pwd null 체크
            if (requestDTO.pwd() == null) {
                throw new Exception400(ExceptionMessage.INVALID_INPUT_VALUE.getMessage());
            }

            // pwd 암호화
            String encodedPwd = passwordEncoder.encode(requestDTO.pwd());

            // 파일 처리
            MultipartFile mainImgFile = requestDTO.mainImgFile();
            MultipartFile subImgFile = requestDTO.subImgFile();

            log.info("파일 업로드 시작 - 메인이미지: {}, 서브이미지: {}", 
                mainImgFile != null ? mainImgFile.getOriginalFilename() : "없음",
                subImgFile != null ? subImgFile.getOriginalFilename() : "없음");

            // 메인 이미지 처리
            if (mainImgFile != null && !mainImgFile.isEmpty()) {
                mainImgPath = saveFile(mainImgFile);
                log.info("메인 이미지 저장 완료: {}", mainImgPath);
            } else if (requestDTO.mainImg() != null) {
                // 기본 이미지 선택한 경우
                mainImgPath = requestDTO.mainImg();
                log.info("기본 메인 이미지 선택: {}", mainImgPath);
            }

            // 서브 이미지 처리
            if (subImgFile != null && !subImgFile.isEmpty()) {
                subImgPath = saveFile(subImgFile);
                log.info("서브 이미지 저장 완료: {}", subImgPath);
            } else if (requestDTO.subImg() != null) {
                // 기본 이미지 선택한 경우
                subImgPath = requestDTO.subImg();
                log.info("기본 서브 이미지 선택: {}", subImgPath);
            }

            // 엔티티 생성 및 저장
            Member member = requestDTO.toEntity(encodedPwd);
            member.setMainImg(mainImgPath);
            member.setSubImg(subImgPath);
            
            memberRepository.save(member);
            log.info("회원 저장 완료 - ID: {}, 메인이미지: {}, 서브이미지: {}", 
                member.getUserId(), mainImgPath, subImgPath);

        } catch (Exception e) {
            // 업로드된 파일이 있다면 삭제
            if (mainImgPath != null) {
                FileUtils.deleteFile(mainImgPath, uploadDirPath);
            }
            if (subImgPath != null) {
                FileUtils.deleteFile(subImgPath, uploadDirPath);
            }
            
            log.error("회원 저장 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("회원 저장에 실패했습니다: " + e.getMessage());
        }
    }
}

