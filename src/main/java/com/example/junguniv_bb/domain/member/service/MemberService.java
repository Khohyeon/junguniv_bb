package com.example.junguniv_bb.domain.member.service;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb.domain.member.dto.MemberDetailResDTO;
import com.example.junguniv_bb.domain.member.dto.MemberSaveReqDTO;
import com.example.junguniv_bb.domain.member.dto.MemberUpdateReqDTO;
import com.example.junguniv_bb.domain.member.model.Member;
import com.example.junguniv_bb.domain.member.model.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    /* DI */
    private final MemberRepository memberRepository;
    // private final PasswordEncoder passwordEncoder;

    /* 다중삭제 */


    /* 삭제 */


    /* 수정 */
    @Transactional
    public void memberUpdate(Long id, MemberUpdateReqDTO reqDTO) {

        // DB조회
        Member memberPS = memberRepository.findById(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_MEMBER.getMessage()));

        // // 비밀번호가 입력된 경우에만 암호화하여 업데이트
        // String encodedPwd = reqDTO.pwd() != null && !reqDTO.pwd().isEmpty()
        // ? passwordEncoder.encode(reqDTO.pwd())
        // : memberPS.getPwd();

        // 트랜잭션 처리
        // reqDTO.updateEntity(memberPS);
    }

    /* 조회 */
    public MemberDetailResDTO memberDetail(Long id
            // 스프링 시큐리티 로그인한 사람의 정보 가져오기
            // ,CustomUserDetails userDetails
    ) {
        Member memberPS = memberRepository.findById(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_MEMBER.getMessage()));

        // CustomUserDetails에서 현재 메뉴 IDX와 권한 레벨을 가져옴


        // 개인정보 접근 권한 확인


        return new MemberDetailResDTO(memberPS);
    }


    /* 등록 */
    @Transactional
    public ResponseEntity<?> memberSave(MemberSaveReqDTO requestDTO) {


        return null;
    }

}
