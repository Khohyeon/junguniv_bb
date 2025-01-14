package com.example.junguniv_bb.domain.member.controller;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.member.dto.MemberDetailResDTO;
import com.example.junguniv_bb.domain.member.dto.MemberSaveReqDTO;
import com.example.junguniv_bb.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage/member/api/")
@Slf4j
public class MemberRestController {

    /* DI */
    private final MemberService memberService;

    /* 다중삭제 */


    /* 삭제 */


    /* 수정 */


    /* 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id
            // ,@AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        // 서비스 호출
        MemberDetailResDTO responseDTO = memberService.memberDetail(id);

        return ResponseEntity.ok(responseDTO);
    }


    /* 등록 */
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid MemberSaveReqDTO requestDTO, Errors errors) {

        if (errors.hasErrors()) {
            log.warn(errors.getAllErrors()
                    .get(0)
                    .getDefaultMessage());
            throw new Exception400(errors.getAllErrors()
                    .get(0)
                    .getDefaultMessage());
        }

        // 서비스 호출
        memberService.memberSave(requestDTO);

        /**
         * 패스워드가 담긴 DTO를 반환하면 취약점 발생
         * 이후 반환값을 메세지로 수정
         */
        return ResponseEntity.ok((APIUtils.success("회원 등록이 완료됐습니다.")));
    }

}
