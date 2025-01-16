package com.example.junguniv_bb.domain.member.service;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb._core.security.CustomUserDetails;
import com.example.junguniv_bb.domain.member._enum.UserType;
import com.example.junguniv_bb.domain.member.dto.*;
import com.example.junguniv_bb.domain.member.model.Member;
import com.example.junguniv_bb.domain.member.model.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    /* DI */
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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

    /* 다중삭제 */
    @Transactional
    public void memberDeleteList(List<Long> idList) {
        
        // DB조회
        List<Member> memberListPS = memberRepository.findAllById(idList);

        if(memberListPS.size() != idList.size()) {
            throw new Exception400(ExceptionMessage.NOT_FOUND_MEMBER.getMessage());
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
        String encodedPwd = reqDTO.pwd() != null && !reqDTO.pwd().isEmpty()
        ? passwordEncoder.encode(reqDTO.pwd())
        : memberPS.getPwd();

        // 트랜잭션 처리
        reqDTO.updateEntity(memberPS, encodedPwd);
    }

    /* 조회 */
    public MemberDetailResDTO memberDetail(Long id, CustomUserDetails customUserDetails) {

        // DB조회
        Member memberPS = memberRepository.findById(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_MEMBER.getMessage()));

        // CustomUserDetails에서 현재 메뉴 IDX와 권한 레벨을 가져옴
        Long menuIdx = customUserDetails.getCurrentMenuIdx();
        Long authLevelIdx = customUserDetails.getAuthLevel();

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

        // pwd null 체크
        if (requestDTO.pwd() == null) {
            throw new Exception400(ExceptionMessage.INVALID_INPUT_VALUE.getMessage());
        }

        // pwd 암호화
        String encodedPwd = passwordEncoder.encode(requestDTO.pwd());

        // 비지니스 로직
        memberRepository.save(requestDTO.toEntity(encodedPwd));
    }
}

