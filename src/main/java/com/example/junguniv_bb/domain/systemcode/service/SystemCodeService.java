package com.example.junguniv_bb.domain.systemcode.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb.domain.systemcode.dto.SystemCodeDetailResDTO;
import com.example.junguniv_bb.domain.systemcode.dto.SystemCodePageResDTO;
import com.example.junguniv_bb.domain.systemcode.dto.SystemCodeSaveReqDTO;
import com.example.junguniv_bb.domain.systemcode.dto.SystemCodeUpdateReqDTO;
import com.example.junguniv_bb.domain.systemcode.model.SystemCode;
import com.example.junguniv_bb.domain.systemcode.model.SystemCodeRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SystemCodeService {

    /* DI */
    private final SystemCodeRepository systemCodeRepository;
    

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

        systemCodeRepository.save(reqDTO.toEntity());
    }
}
