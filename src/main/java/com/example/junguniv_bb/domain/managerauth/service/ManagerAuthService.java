package com.example.junguniv_bb.domain.managerauth.service;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb.domain.authlevel.model.AuthLevelRepository;
import com.example.junguniv_bb.domain.managerauth.dto.ManagerAuthDetailResDTO;
import com.example.junguniv_bb.domain.managerauth.dto.ManagerAuthPageResDTO;
import com.example.junguniv_bb.domain.managerauth.dto.ManagerAuthSaveReqDTO;
import com.example.junguniv_bb.domain.managerauth.dto.ManagerAuthUpdateReqDTO;
import com.example.junguniv_bb.domain.managerauth.model.ManagerAuth;
import com.example.junguniv_bb.domain.managerauth.model.ManagerAuthRepository;
import com.example.junguniv_bb.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ManagerAuthService {

    private final ManagerAuthRepository managerAuthRepository;
    private final AuthLevelRepository authLevelRepository;

    /* ManagerAuth 업서트 (업데이트 또는 삽입) */
    @Transactional
    public void managerAuthUpsert(ManagerAuthUpdateReqDTO reqDTO) {
        Long menuIdx = reqDTO.menuIdx();
        Long authLevel = reqDTO.authLevel();

        // 기존 엔티티 조회
        ManagerAuth managerAuthPS = managerAuthRepository.findByMenuIdxAndAuthLevel(menuIdx, authLevel);

        if (managerAuthPS != null) {
            // 기존 엔티티 업데이트
            reqDTO.updateEntity(managerAuthPS);
        } else {
            // 새로운 엔티티 생성 및 저장
            ManagerAuth newManagerAuthForSave = reqDTO.toEntity();
            managerAuthRepository.save(newManagerAuthForSave);
            managerAuthPS = newManagerAuthForSave;
        }
    }


    /* AuthLevel의 ManagerAuth 목록 가져오기 */
    public List<ManagerAuth> findAllByAuthLevel(Long authLevel) {
        return managerAuthRepository.findAllByAuthLevel(authLevel);
    }


    /* ManagerAuth 목록 */
    public ManagerAuthPageResDTO managerAuthPage(Member member, Pageable pageable, String menuName) {
        // TODO 권한 체크

        // DB 조회 (수정된 부분)
        Page<ManagerAuth> managerAuthPagePS;

        if (menuName != null) {
            // 검색 키워드가 있는 경우, 검색된 키워드로 조회 (countQuery 분리)
            managerAuthPagePS = managerAuthRepository.findAllByMenuNameWithoutParent(menuName, pageable);
        } else {
            // 검색 키워드가 없는 경우, 모든 내용 조회 (countQuery 분리)
            managerAuthPagePS = managerAuthRepository.findAllWithoutParent(pageable);
        }

        return new ManagerAuthPageResDTO(managerAuthPagePS);
    }


    /* ManagerAuth 삭제 */
    @Transactional
    public void managerAuthDelete(Long id) {
        // DB 조회
        ManagerAuth managerAuthPS = managerAuthRepository.findById(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_MANAGER_AUTH.getMessage()));

        // 트랜잭션 처리
        managerAuthRepository.delete(managerAuthPS);
    }


    /* ManagerAuth 수정 */
    @Transactional
    public void managerAuthUpdate(Long id, ManagerAuthUpdateReqDTO reqDTO) {
        // DB 조회
        ManagerAuth managerAuthPS = managerAuthRepository.findById(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_MANAGER_AUTH.getMessage()));

        // 엔티티 업데이트
        reqDTO.updateEntity(managerAuthPS);
    }


    /* ManagerAuth 조회 */
    public ManagerAuthDetailResDTO managerAuthDetail(Long id) {
        // DB 조회
        ManagerAuth managerAuthPS = managerAuthRepository.findById(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_MANAGER_AUTH.getMessage()));

        return ManagerAuthDetailResDTO.from(managerAuthPS);
    }


    /* ManagerAuth 등록 */
    @Transactional
    public void managerAuthSave(List<ManagerAuthSaveReqDTO> reqDTOList) {
        List<ManagerAuth> managerAuthList = reqDTOList.stream().map(ManagerAuthSaveReqDTO::toEntity) // 각 DTO를 엔티티로 변환
                .collect(Collectors.toList());

        // 트랜잭션 처리
        managerAuthRepository.saveAll(managerAuthList); // 엔티티 리스트를 저장
    }


    /* 권한 확인 메서드 */
    @Transactional
    public boolean hasPermission(Long menuIdx, Long authLevel, String action) {
        ManagerAuth managerAuth = managerAuthRepository.findByMenuIdxAndAuthLevel(menuIdx, authLevel);
        if (managerAuth == null) {
            return false;
        }

        return switch (action) {
            case "READ" -> managerAuth.getMenuReadAuth() != null && managerAuth.getMenuReadAuth() == 1;
            case "WRITE" -> managerAuth.getMenuWriteAuth() != null && managerAuth.getMenuWriteAuth() == 1;
            case "MODIFY" -> managerAuth.getMenuModifyAuth() != null && managerAuth.getMenuModifyAuth() == 1;
            case "DELETE" -> managerAuth.getMenuDeleteAuth() != null && managerAuth.getMenuDeleteAuth() == 1;
            default -> false;
        };
    }

    /**
     * 개인정보 접근 권한을 확인하는 메서드
     */
    public boolean hasPrivacyPermission(Long menuIdx, Long authLevel) {
        // 1. 기본 읽기 권한 체크
        if (!hasPermission(menuIdx, authLevel, "READ")) {
            return false;
        }

        // 2. 개인정보 접근 권한 체크
        ManagerAuth managerAuth = managerAuthRepository.findByMenuIdxAndAuthLevel(menuIdx, authLevel);
        if (managerAuth == null) {
            return false;
        }

        // 3. 메뉴의 개인정보 포함 여부와 권한 레벨의 개인정보 조회 권한 확인
        return authLevelRepository.findByAuthLevel(authLevel)
                .map(authLevelEntity -> "Y".equals(authLevelEntity.getChkViewJumin()))
                .orElse(false);
    }
}

