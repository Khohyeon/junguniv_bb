package com.example.junguniv_bb.domain.authlevel.service;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb._core.util.PrivacyMaskingUtil;
import com.example.junguniv_bb.domain.authlevel.dto.AuthLevelDetailResDTO;
import com.example.junguniv_bb.domain.authlevel.dto.AuthLevelPageResDTO;
import com.example.junguniv_bb.domain.authlevel.dto.AuthLevelSaveReqDTO;
import com.example.junguniv_bb.domain.authlevel.dto.AuthLevelUpdateReqDTO;
import com.example.junguniv_bb.domain.authlevel.model.AuthLevel;
import com.example.junguniv_bb.domain.authlevel.model.AuthLevelRepository;
import com.example.junguniv_bb.domain.managerauth.model.ManagerAuth;
import com.example.junguniv_bb.domain.managerauth.model.ManagerAuthRepository;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenuRepository;
import com.example.junguniv_bb.domain.member.model.Member;
import com.example.junguniv_bb.domain.member.model.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthLevelService {

    /* DI */
    private final AuthLevelRepository authLevelRepository;
    private final ManagerAuthRepository managerAuthRepository;
    private final ManagerMenuRepository managerMenuRepository;
    private final MemberRepository memberRepository;
    private final PrivacyMaskingUtil privacyMaskingUtil;


    /* AuthLevel 모두 조회 */
    public List<AuthLevel> getAllAuthLevel() {
        return authLevelRepository.findAll();
    }

    // 권한 레벨로 AuthLevel 찾기
    public Optional<AuthLevel> findByAuthLevel(Long authLevel) {
        return authLevelRepository.findByAuthLevel(authLevel);
    }



    /* 관리자 권한에 따른 접근 제어 */
    public boolean hasPermission(Long menuIdx, Long authLevel, String action) {
        ManagerAuth managerAuth = managerAuthRepository.findByMenuIdxAndAuthLevel(menuIdx, authLevel);
        if (managerAuth == null) return false;

        return switch (action) {
            case "READ" -> managerAuth.getMenuReadAuth() != null && managerAuth.getMenuReadAuth() == 1;
            case "WRITE" -> managerAuth.getMenuWriteAuth() != null && managerAuth.getMenuWriteAuth() == 1;
            case "MODIFY" -> managerAuth.getMenuModifyAuth() != null && managerAuth.getMenuModifyAuth() == 1;
            case "DELETE" -> managerAuth.getMenuDeleteAuth() != null && managerAuth.getMenuDeleteAuth() == 1;
            default -> false;
        };
    }

    /* 기본적인 개인정보 접근 권한 확인 (관리자권한 및 메뉴) */
    public boolean hasPrivacyAccessPermission(Long menuIdx, Long authLevelIdx) {
        // 권한 레벨 조회
        AuthLevel authLevelPS = authLevelRepository.findByAuthLevel(authLevelIdx)
                .orElseThrow(() -> new Exception400("권한 레벨을 찾을 수 없습니다."));
                
        // 메뉴 조회
        ManagerMenu managerMenuPS = managerMenuRepository.findById(menuIdx)
                .orElseThrow(() -> new Exception400("메뉴를 찾을 수 없습니다."));
        
        // 1. 권한레벨의 개인정보 조회 권한이 없으면 모든 개인정보 마스킹
        if (!"Y".equals(authLevelPS.getChkViewJumin())) {
            return false;
        }

        // 2. 메뉴의 개인정보 조회 허용 여부 확인
        if (!"Y".equals(managerMenuPS.getChkPerson())) {
            return false;
        }

        return true;
    }

    /* 특정 개인정보 항목에 대한 접근 권한 확인 */
    public boolean hasPrivacyAccessPermission(Long menuIdx, Long authLevelIdx, String privacyType) {
        // 1. 기본적인 개인정보 접근 권한 확인
        if (!hasPrivacyAccessPermission(menuIdx, authLevelIdx)) {
            return false;
        }

        // 2. 메뉴 조회
        ManagerMenu managerMenuPS = managerMenuRepository.findById(menuIdx)
                .orElseThrow(() -> new Exception400("메뉴를 찾을 수 없습니다."));

        // 3. 메뉴의 개인정보 항목 확인
        String personInfor = managerMenuPS.getPersonInfor();
        if (personInfor == null || personInfor.isEmpty()) {
            return false;
        }

        // 4. 개인정보 항목들을 배열로 분리
        String[] personInforItems = personInfor.split(",");
        
        // 5. 요청된 개인정보 유형이 허용된 항목에 포함되어 있는지 확인
        for (String item : personInforItems) {
            if (item.trim().equals(privacyType)) {
                return true;
            }
        }

        return false;
    }


    /* AuthLevel 페이징 및 검색 */
    public AuthLevelPageResDTO authLevelPage(
            // Member member,
            Pageable pageable, String authLevelName) {

        // TODO 권한에 따른 분기

        // DB 조회
        Page<AuthLevel> authLevelPagePS;

        if (authLevelName != null) {
            // 검색 키워드 OOO, 검색된 키워드 조회
            authLevelPagePS = authLevelRepository.findByAuthLevelNameContainingIgnoreCase(authLevelName, pageable);
        } else {
            // 검색 키워드 XXX, 모든 내용 조회
            authLevelPagePS = authLevelRepository.findAll(pageable);
        }

        // 각 AuthLevel 값을 가진 Member 수를 계산
        List<Long> authLevels = authLevelPagePS.getContent().stream()
                .map(AuthLevel::getAuthLevel)
                .collect(Collectors.toList());

        // Member 테이블에서 각 AuthLevel 별 회원 수를 조회
        Map<Long, Long> memberCountMap = memberRepository.countMembersByAuthLevel(authLevels);

        return AuthLevelPageResDTO.from(authLevelPagePS, memberCountMap);
    }

    /* AuthLevel 삭제 */
    @Transactional
    public void authLevelDelete(Long id) {
        // DB 조회
        AuthLevel authLevelPS = authLevelRepository.findById(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_AUTH_LEVEL.getMessage()));

        Long authLevelValue = authLevelPS.getAuthLevel();
        
        // 연결된 관리자 수 확인
        Map<Long, Long> memberCountMap = memberRepository.countMembersByAuthLevel(List.of(authLevelValue));
        Long connectedMemberCount = memberCountMap.getOrDefault(authLevelValue, 0L);
        
        if(connectedMemberCount > 0) {
            throw new Exception400(ExceptionMessage.EXISTS_LINKED_MEMBER_AUTH_LEVEL.getMessage());
        }

        // 관련 ManagerAuth 삭제
        managerAuthRepository.deleteAllByAuthLevel(authLevelValue);

        // Member의 authLevel을 null로 설정
        memberRepository.setAuthLevelToNullForMembers(authLevelValue);

        // AuthLevel 삭제
        authLevelRepository.delete(authLevelPS);
    }


    /* AuthLevel 수정 */
    @Transactional
    public void authLevelUpdate(Long id, AuthLevelUpdateReqDTO reqDTO) {

        // DB 조회
        AuthLevel authLevelPS = authLevelRepository.findById(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_AUTH_LEVEL.getMessage()));

        Long oldAuthLevel = authLevelPS.getAuthLevel();
        Long newAuthLevel = reqDTO.authLevel();

        // 중복체크 (변경하려는 authLevel 값이 기존의 다른 엔티티에 존재하는지 확인)
        if (authLevelRepository.existsByAuthLevelAndAuthLevelIdxNot(reqDTO.authLevel(), id)) {
            throw new Exception400(ExceptionMessage.EXISTS_AUTH_LEVEL.getMessage());
        }

        // 트랜잭션 처리
        reqDTO.updateEntity(authLevelPS);

        // authLevel 값이 변경되었는지 확인
        if (!oldAuthLevel.equals(newAuthLevel)) {
            // ManagerAuth의 authLevel 값 업데이트
            managerAuthRepository.updateAuthLevelForAuthLevel(oldAuthLevel, newAuthLevel);

            // Member의 authLevel 값 업데이트
            memberRepository.updateAuthLevelForMembers(oldAuthLevel, newAuthLevel);
        }
    }


    /* AuthLevel 조회 */
    public AuthLevelDetailResDTO authLevelDetail(Long id) {
        // SQL 인젝션 방지를 위한 유효성 검증
        if (id == null || id <= 0) {
            throw new Exception400(ExceptionMessage.INVALID_INPUT_VALUE.getMessage());
        }
        
        // DB 조회
        AuthLevel authLevelPS = authLevelRepository.findById(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_AUTH_LEVEL.getMessage()));

        return AuthLevelDetailResDTO.from(authLevelPS);
    }

    /* AuthLevel 등록 */
    @Transactional
    public void authLevelSave(AuthLevelSaveReqDTO reqDTO) {

        // 중복 체크
        if (authLevelRepository.existsByAuthLevel(reqDTO.authLevel())) {
            throw new Exception400(ExceptionMessage.EXISTS_AUTH_LEVEL.getMessage());
        }

        // 트랜잭션 처리
        authLevelRepository.save(reqDTO.toEntity());

    }



    /**
     * Member 객체의 개인정보를 마스킹 처리하는 메서드
     */
    public Member maskMemberPrivateInfo(Member member) {
        Member maskedMember = new Member();
        BeanUtils.copyProperties(member, maskedMember);
        
        // 개인정보 필드 마스킹 처리 -> null 값에 마스킹 처리시 에러 발생함
        maskedMember.setName(privacyMaskingUtil.maskName(member.getName()));
        maskedMember.setEmail(privacyMaskingUtil.maskEmail(member.getEmail()));
        maskedMember.setTelHome(privacyMaskingUtil.maskTelHome(member.getTelHome()));
        maskedMember.setTelMobile(privacyMaskingUtil.maskTelMobile(member.getTelMobile()));
        maskedMember.setZipcode(privacyMaskingUtil.maskZipcode(member.getZipcode()));
        maskedMember.setAddr1(privacyMaskingUtil.maskAddr1(member.getAddr1()));
        maskedMember.setAddr2(privacyMaskingUtil.maskAddr2(member.getAddr2()));
        maskedMember.setBankNumber(privacyMaskingUtil.maskBankNumBer(member.getBankNumber()));
        maskedMember.setResidentNumber(privacyMaskingUtil.maskResidentNumber(member.getResidentNumber()));
        
        return maskedMember;
    }

    /**
     * Member 목록의 개인정보를 마스킹 처리하는 메서드
     */
    public List<Member> maskMemberListPrivateInfo(List<Member> members) {
        return members.stream()
                .map(this::maskMemberPrivateInfo)
                .collect(Collectors.toList());
    }

    //
}
