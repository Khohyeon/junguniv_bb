package com.example.junguniv_bb.domain.member.service;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb._core.exception.ValidExceptionMessage;
import com.example.junguniv_bb._core.security.CustomUserDetails;
import com.example.junguniv_bb._core.util.APIUtils;
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
import org.springframework.http.HttpStatus;
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
import java.util.stream.Collectors;

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
    public ResponseEntity<?> memberPage(String referer, Pageable pageable) {
        // 페이지 조회
        Page<Member> memberPagePS;

        // Referer URL에서 userType 결정
        UserType userType = null;

        if (referer != null) {
            // 주소록 페이지인 경우
            if (referer.contains("/address")) {
                memberPagePS = memberRepository.findAll(pageable);
                return ResponseEntity.ok(new MemberAddressPageResDTO(memberPagePS));
            }

            if (referer.contains("/student")) {
                userType = UserType.STUDENT;
            } else if (referer.contains("/teacher")) {
                userType = UserType.TEACHER;
            } else if (referer.contains("/company")) {
                userType = UserType.COMPANY;
            } else if (referer.contains("/admin")) {
                userType = UserType.ADMIN;
            }
        }

        // UserType에 따른 페이지 조회
        memberPagePS = (userType != null) ?
            memberRepository.findByUserType(userType, pageable) :
            memberRepository.findAll(pageable);

        // UserType에 따른 DTO 변환 및 반환
        if (userType != null) {
            if (userType == UserType.STUDENT) {
                return ResponseEntity.ok(new MemberStudentPageResDTO(memberPagePS));
            } else if (userType == UserType.TEACHER) {
                return ResponseEntity.ok(new MemberTeacherPageResDTO(memberPagePS));
            } else if (userType == UserType.COMPANY) {
                return ResponseEntity.ok(new MemberCompanyPageResDTO(memberPagePS));
            } else if (userType == UserType.ADMIN) {
                return ResponseEntity.ok(new MemberAdminPageResDTO(memberPagePS));
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

    /* 교강사 검색 */
    @Transactional(readOnly = true)
    public ResponseEntity<?> searchTeachers(MemberTeacherSearchReqDTO searchDTO, Pageable pageable) {
        try {
            Page<Member> memberPagePS = memberRepository.searchTeachers(
                searchDTO.name(),
                searchDTO.userId(),
                searchDTO.jobEmployeeType(),
                searchDTO.telMobile(),
                searchDTO.email(),
                pageable
            );
            return ResponseEntity.ok(new MemberPageResDTO(memberPagePS));
        } catch (Exception e) {
            log.error("교강사 검색 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("교강사 검색 중 오류가 발생했습니다.");
        }
    }

    /* 기업 검색 */
    @Transactional(readOnly = true)
    public ResponseEntity<?> searchCompanies(MemberCompanySearchReqDTO searchDTO, Pageable pageable) {
        try {
            Page<Member> memberPagePS = memberRepository.searchCompanies(
                searchDTO.jobName(),
                searchDTO.userId(),
                searchDTO.jobNumber(),
                searchDTO.contractorName(),
                searchDTO.contractorTel(),
                searchDTO.contractorEtc(),
                searchDTO.jobScale(),
                pageable
            );
            return ResponseEntity.ok(new MemberCompanyPageResDTO(memberPagePS));
        } catch (Exception e) {
            log.error("기업 검색 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("기업 검색 중 오류가 발생했습니다.");
        }
    }

    /* 관리자 검색 */
    @Transactional(readOnly = true)
    public ResponseEntity<?> searchAdmins(MemberAdminSearchReqDTO searchDTO, Pageable pageable) {
        try {
            Page<Member> memberPagePS = memberRepository.searchAdmins(
                searchDTO.name(),
                searchDTO.userId(),
                searchDTO.jobCourseDuty(),
                pageable
            );
            return ResponseEntity.ok(new MemberAdminPageResDTO(memberPagePS));
        } catch (Exception e) {
            log.error("관리자 검색 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자 검색 중 오류가 발생했습니다.");
        }
    }

    /* 주소록 검색 */
    @Transactional(readOnly = true)
    public ResponseEntity<?> searchMemberAddress(MemberAddressSearchReqDTO searchDTO, Pageable pageable) {
        try {
            // 모든 검색 조건이 비어있으면 전체 목록 반환
            if (isEmptySearchCondition(searchDTO)) {
                Page<Member> memberPagePS = memberRepository.findAll(pageable);
                return ResponseEntity.ok(new MemberAddressPageResDTO(memberPagePS));
            }

            // 검색 조건이 있는 경우 검색 실행
            Page<Member> memberPagePS = memberRepository.searchAddress(
                searchDTO.name(),
                searchDTO.userId(),
                searchDTO.address(),
                searchDTO.telMobile(),
                searchDTO.email(),
                searchDTO.jobName(),
                pageable
            );
            return ResponseEntity.ok(new MemberAddressPageResDTO(memberPagePS));
        } catch (Exception e) {
            log.error("주소록 검색 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주소록 검색 중 오류가 발생했습니다.");
        }
    }

    // 검색 조건이 모두 비어있는지 확인하는 메서드
    private boolean isEmptySearchCondition(MemberAddressSearchReqDTO searchDTO) {
        return (searchDTO.name() == null || searchDTO.name().trim().isEmpty()) &&
               (searchDTO.userId() == null || searchDTO.userId().trim().isEmpty()) &&
               (searchDTO.address() == null || searchDTO.address().trim().isEmpty()) &&
               (searchDTO.telMobile() == null || searchDTO.telMobile().trim().isEmpty()) &&
               (searchDTO.email() == null || searchDTO.email().trim().isEmpty()) &&
               (searchDTO.jobName() == null || searchDTO.jobName().trim().isEmpty());
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

        // 메인 이미지 처리
        if (reqDTO.mainImg() != null && reqDTO.mainImg().equals("custom")) {
            if (reqDTO.mainImgFile() != null && !reqDTO.mainImgFile().isEmpty()) {
                String mainImgPath = saveFile(reqDTO.mainImgFile());
                memberPS.setMainImg(mainImgPath);
                memberPS.setMainImgName(reqDTO.mainImgFile().getOriginalFilename());
            }
        } else if (reqDTO.mainImg() != null) {
            // 기본 이미지인 경우 파일명만 저장
            memberPS.setMainImg(reqDTO.mainImg());
            memberPS.setMainImgName(reqDTO.mainImg());
        } else {
            // 이미지를 삭제하는 경우
            memberPS.setMainImg(null);
            memberPS.setMainImgName(null);
        }

        // 서브 이미지 처리
        if (reqDTO.subImg() != null && reqDTO.subImg().equals("custom")) {
            if (reqDTO.subImgFile() != null && !reqDTO.subImgFile().isEmpty()) {
                String subImgPath = saveFile(reqDTO.subImgFile());
                memberPS.setSubImg(subImgPath);
                memberPS.setSubImgName(reqDTO.subImgFile().getOriginalFilename());
            }
        } else if (reqDTO.subImg() != null) {
            // 기본 이미지인 경우 파일명만 저장
            memberPS.setSubImg(reqDTO.subImg());
            memberPS.setSubImgName(reqDTO.subImg());
        } else {
            // 이미지를 삭제하는 경우
            memberPS.setSubImg(null);
            memberPS.setSubImgName(null);
        }

        // 사업자등록증 처리
        if (reqDTO.fnameSaupFile() != null && !reqDTO.fnameSaupFile().isEmpty()) {
            String fnameSaupPath = saveFile(reqDTO.fnameSaupFile());
            memberPS.setFnameSaup(fnameSaupPath);
            memberPS.setFnameSaupName(reqDTO.fnameSaupFile().getOriginalFilename());
        } else if (reqDTO.fnameSaup() == null || reqDTO.fnameSaup().isEmpty()) {
            memberPS.setFnameSaup(null);
            memberPS.setFnameSaupName(null);
        }

        // 로고 처리
        if (reqDTO.fnameLogoFile() != null && !reqDTO.fnameLogoFile().isEmpty()) {
            String fnameLogoPath = saveFile(reqDTO.fnameLogoFile());
            memberPS.setFnameLogo(fnameLogoPath);
            memberPS.setFnameLogoName(reqDTO.fnameLogoFile().getOriginalFilename());
        } else if (reqDTO.fnameLogo() == null || reqDTO.fnameLogo().isEmpty()) {
            memberPS.setFnameLogo(null);
            memberPS.setFnameLogoName(null);
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
        String fnameSaupPath = null;
        String fnameLogoPath = null;

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
            MultipartFile fnameSaupFile = requestDTO.fnameSaupFile();
            MultipartFile fnameLogoFile = requestDTO.fnameLogoFile();

            log.info("파일 업로드 시작 - 메인이미지: {}, 서브이미지: {}, 사업자등록증: {}, 로고: {}",
                mainImgFile != null ? mainImgFile.getOriginalFilename() : "없음",
                subImgFile != null ? subImgFile.getOriginalFilename() : "없음",
                fnameSaupFile != null ? fnameSaupFile.getOriginalFilename() : "없음",
                fnameLogoFile != null ? fnameLogoFile.getOriginalFilename() : "없음");

            // 엔티티 생성
            Member member = requestDTO.toEntity(encodedPwd);

            // 메인 이미지 처리
            if (requestDTO.mainImg() != null && requestDTO.mainImg().equals("custom")) {
                if (mainImgFile != null && !mainImgFile.isEmpty()) {
                    mainImgPath = saveFile(mainImgFile);
                    member.setMainImg(mainImgPath);
                    member.setMainImgName(mainImgFile.getOriginalFilename());
                }
            } else if (requestDTO.mainImg() != null) {
                // 기본 이미지인 경우 파일명만 저장
                member.setMainImg(requestDTO.mainImg());
                member.setMainImgName(requestDTO.mainImg());
            }

            // 서브 이미지 처리
            if (requestDTO.subImg() != null && requestDTO.subImg().equals("custom")) {
                if (subImgFile != null && !subImgFile.isEmpty()) {
                    subImgPath = saveFile(subImgFile);
                    member.setSubImg(subImgPath);
                    member.setSubImgName(subImgFile.getOriginalFilename());
                }
            } else if (requestDTO.subImg() != null) {
                // 기본 이미지인 경우 파일명만 저장
                member.setSubImg(requestDTO.subImg());
                member.setSubImgName(requestDTO.subImg());
            }

            // 사업자등록증 처리
            if (fnameSaupFile != null && !fnameSaupFile.isEmpty()) {
                fnameSaupPath = saveFile(fnameSaupFile);
                member.setFnameSaup(fnameSaupPath);
                member.setFnameSaupName(fnameSaupFile.getOriginalFilename());
            }

            // 로고 처리
            if (fnameLogoFile != null && !fnameLogoFile.isEmpty()) {
                fnameLogoPath = saveFile(fnameLogoFile);
                member.setFnameLogo(fnameLogoPath);
                member.setFnameLogoName(fnameLogoFile.getOriginalFilename());
            }

            memberRepository.save(member);
            log.info("회원 저장 완료 - ID: {}, 메인이미지: {}, 서브이미지: {}, 사업자등록증: {}, 로고: {}",
                member.getUserId(), member.getMainImg(), member.getSubImg(), fnameSaupPath, fnameLogoPath);

        } catch (Exception e) {
            // 업로드된 파일이 있다면 삭제 (기본 이미지는 제외)
            if (mainImgPath != null) FileUtils.deleteFile(mainImgPath, uploadDirPath);
            if (subImgPath != null) FileUtils.deleteFile(subImgPath, uploadDirPath);
            if (fnameSaupPath != null) FileUtils.deleteFile(fnameSaupPath, uploadDirPath);
            if (fnameLogoPath != null) FileUtils.deleteFile(fnameLogoPath, uploadDirPath);

            log.error("회원 저장 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("회원 저장에 실패했습니다: " + e.getMessage());
        }
    }

    /* 주소록 일괄 출력용 데이터 조회 */
    public ResponseEntity<?> getAddressLabels(List<Long> memberIds) {
        if (memberIds == null || memberIds.isEmpty()) {
            throw new Exception400("선택된 회원이 없습니다.");
        }

        List<Member> members = memberRepository.findAllById(memberIds);
        
        if (members.size() != memberIds.size()) {
            throw new Exception400("일부 회원 정보를 찾을 수 없습니다.");
        }

        // null-safe 정렬
        members.sort((a, b) -> {
            String nameA = a.getName() != null ? a.getName() : "";
            String nameB = b.getName() != null ? b.getName() : "";
            return nameA.compareTo(nameB);
        });

        List<MemberAddressLabelDTO> labels = members.stream()
            .map(MemberAddressLabelDTO::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok(APIUtils.success(labels));
    }
}

