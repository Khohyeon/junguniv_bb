package com.example.junguniv_bb.domain.systemcode.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb.domain.systemcode.dto.SystemCodeDetailResDTO;
import com.example.junguniv_bb.domain.systemcode.dto.SystemCodePageResDTO;
import com.example.junguniv_bb.domain.systemcode.dto.SystemCodeSaveReqDTO;
import com.example.junguniv_bb.domain.systemcode.dto.SystemCodeUpdateReqDTO;
import com.example.junguniv_bb.domain.systemcode.model.SystemCode;
import com.example.junguniv_bb.domain.systemcode.model.SystemCodeRepository;

import jakarta.annotation.PostConstruct;

import com.example.junguniv_bb._core.util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SystemCodeService {

    /* DI */
    private final SystemCodeRepository systemCodeRepository;
    
    @Value("${file.upload.directories.system-code}")
    private String uploadDirPath;

    @Value("${file.upload.directories.system-code-temp}")
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

    /* 관리자 로고 이미지 저장 */
    @Transactional
    public void saveAdminLogo(MultipartFile file) {
        try {
            // 이미지 파일 검증
            if (!FileUtils.isImageFile(file)) {
                throw new Exception400("이미지 파일만 업로드 가능합니다.");
            }

            // 파일 크기 검증 (5MB)
            if (!FileUtils.validateFileSize(file, 5 * 1024 * 1024)) {
                throw new Exception400("파일 크기는 5MB를 초과할 수 없습니다.");
            }

            // 파일 저장
            String savedFileName = FileUtils.uploadFile(file, uploadDirPath);

            // 시스템 코드 저장
            SystemCodeSaveReqDTO systemCode = new SystemCodeSaveReqDTO(
                "관리자페이지 로고 이미지",  // systemCodeName
                "이미지 파일 (jpg, png)",  // systemCodeRule
                "기본설정",  // systemCodeGroup
                "ADMIN_LOGO",  // systemCodeKey
                savedFileName  // systemCodeValue
            );

            systemCodeSave(systemCode);

        } catch (Exception e) {
            log.error("로고 이미지 저장 중 오류 발생: {}", e.getMessage());
            throw new Exception400("로고 이미지 저장에 실패했습니다: " + e.getMessage());
        }
    }

    /* 시스템 코드 페이징 및 검색 */
    public SystemCodePageResDTO systemCodePage(Pageable pageable, String systemCodeName) {
        Page<SystemCode> systemCodePagePS;

        if (systemCodeName != null) {
            systemCodePagePS = systemCodeRepository.findBySystemCodeNameContainingIgnoreCase(systemCodeName, pageable);
        } else {
            systemCodePagePS = systemCodeRepository.findAll(pageable);
        }

        return SystemCodePageResDTO.from(systemCodePagePS);
    }

    /* 시스템 코드 다중삭제 */
    @Transactional
    public void systemCodeMultiDelete(List<Long> ids) {

        // 유효성 검사
        if (ids == null || ids.isEmpty()) {
            throw new Exception400(ExceptionMessage.INVALID_INPUT_VALUE.getMessage());
        }

        systemCodeRepository.deleteAllById(ids);
    }

    /* 시스템 코드 삭제 */
    @Transactional
    public void systemCodeDelete(Long id) {
        SystemCode systemCodePS = systemCodeRepository.findById(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_SYSTEM_CODE.getMessage()));

        systemCodeRepository.delete(systemCodePS);
    }

    /* 시스템 코드 수정 */
    @Transactional
    public void systemCodeUpdate(Long id, SystemCodeUpdateReqDTO reqDTO) {

        SystemCode systemCodePS = systemCodeRepository.findById(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_SYSTEM_CODE.getMessage()));

        reqDTO.updateEntity(systemCodePS);
    }

    /* 시스템 코드 조회 */
    public SystemCodeDetailResDTO systemCodeDetail(Long id) {

        if (id == null || id <= 0) {
            throw new Exception400(ExceptionMessage.INVALID_INPUT_VALUE.getMessage());
        }
        
        SystemCode systemCodePS = systemCodeRepository.findById(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_SYSTEM_CODE.getMessage()));

        return SystemCodeDetailResDTO.from(systemCodePS);
    }
    
    /* 시스템 코드 저장 */
    @Transactional
    public void systemCodeSave(SystemCodeSaveReqDTO reqDTO) {
        // 기존 데이터 조회
        Optional<SystemCode> existingCode = systemCodeRepository.findBySystemCodeKeyAndSystemCodeGroup(
            reqDTO.systemCodeKey(),
            reqDTO.systemCodeGroup()
        );

        if (existingCode.isPresent()) {
            // 기존 데이터가 있으면 업데이트
            SystemCode systemCode = existingCode.get();
            systemCode.setSystemCodeName(reqDTO.systemCodeName());
            systemCode.setSystemCodeRule(reqDTO.systemCodeRule());
            systemCode.setSystemCodeValue(reqDTO.systemCodeValue());
            systemCodeRepository.save(systemCode);
        } else {
            // 새로운 데이터 저장
            systemCodeRepository.save(reqDTO.toEntity());
        }
    }

    /**
     * 시스템 코드 조회 - 시스템코드키, 시스템코드그룹
     */
    public Optional<SystemCode> findBySystemCodeKeyAndSystemCodeGroup(String systemCodeKey, String systemCodeGroup) {
        return systemCodeRepository.findBySystemCodeKeyAndSystemCodeGroup(systemCodeKey, systemCodeGroup);
    }

    /**
     * 시스템 코드 조회 - 시스템코드명, 시스템코드그룹
     */
    public Optional<SystemCode> findBySystemCodeNameAndSystemCodeGroup(String systemCodeName, String systemCodeGroup) {
        return systemCodeRepository.findBySystemCodeNameAndSystemCodeGroup(systemCodeName, systemCodeGroup);
    }
}
