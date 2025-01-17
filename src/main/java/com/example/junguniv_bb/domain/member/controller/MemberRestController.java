package com.example.junguniv_bb.domain.member.controller;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.security.CustomUserDetails;
import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.member.dto.*;
import com.example.junguniv_bb.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/member/api/")
@Slf4j
public class MemberRestController {

    /* DI */
    private final MemberService memberService;

    /* 학생 검색 */
    @GetMapping("/student/search")
    public ResponseEntity<?> searchStudents(
            @ModelAttribute MemberStudentSearchReqDTO searchDTO,
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return memberService.searchStudents(searchDTO, pageable);
    }

    /* 아이디 중복 체크 */
    @GetMapping("/idCheck")
    public ResponseEntity<?> idCheck(@RequestParam String userId) {
        // 서비스 호출
        boolean isDuplicate = memberService.checkDuplicateId(userId);
        
        // true일 때가 중복이므로, 응답을 반대로 해야 합니다
        return ResponseEntity.ok(APIUtils.success(!isDuplicate));
    }


    /* 페이지 조회 */
    @GetMapping("/")
    public ResponseEntity<?> memberPage(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                      @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                                      HttpServletRequest request) {

        // Referer 헤더에서 이전 페이지 URL 가져오기
        String referer = request.getHeader("Referer");
        
        // 서비스 호출 (이미 ResponseEntity를 반환하므로 바로 반환)
        return memberService.memberPage(referer, pageable);
    }

    /* 다중삭제 */
    @DeleteMapping
    public ResponseEntity<?> memberDeleteList(@RequestBody List<Long> idList) {

        // 서비스 호출
        memberService.memberDeleteList(idList);

        return ResponseEntity.ok(APIUtils.success("회원 다중 삭제 완료."));
    }

    /* 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> memberDelete(@PathVariable Long id) {

        // 서비스 호출
        memberService.memberDelete(id);

        return ResponseEntity.ok(APIUtils.success("회원 삭제 완료."));
    }


    /* 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<?> memberUpdate(@PathVariable Long id, @RequestBody @Valid MemberUpdateReqDTO requestDTO, Errors errors) {

        if (errors.hasErrors()) {
            log.warn(errors.getAllErrors()
                    .get(0)
                    .getDefaultMessage());
            throw new Exception400(errors.getAllErrors()
                    .get(0)
                    .getDefaultMessage());
        }

        // 서비스 호출
        memberService.memberUpdate(id, requestDTO);

        return ResponseEntity.ok(APIUtils.success("회원 수정 완료."));
    }


    /* 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<?> memberDetail(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        // 서비스 호출
        MemberDetailResDTO responseDTO = memberService.memberDetail(id);

        return ResponseEntity.ok(responseDTO);
    }


    /* 등록 */
    @PostMapping
    public ResponseEntity<?> memberSave(@RequestBody @Valid MemberSaveReqDTO requestDTO, Errors errors) {

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
        return ResponseEntity.ok((APIUtils.success("회원 등록 완료.")));
    }

}
