package com.example.junguniv_bb.domain.member.controller;

import com.example.junguniv_bb.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/member")
@Slf4j
public class MemberController {

    /* DI */
    private final MemberService memberService;



    /**
     * 회원관리 - 등록
     */
    /* 회원관리 > 회원정보관리 > 수강생 등록 */
    @GetMapping("/student/saveForm")
    public String studentSaveForm(Model model) {

        return "/masterpage_sys/member/studentSaveForm";
    }

    /* 회원관리 > 회원정보관리 > 교강사(튜터) 등록 */
    @GetMapping("/teacher/saveForm")
    public String teacherSaveForm(Model model) {

        return "/masterpage_sys/member/teacherSaveForm";
    }

    /* 회원관리 > 회원정보관리 > 위탁기업 등록 */
    @GetMapping("/company/saveForm")
    public String companySaveForm(Model model) {

        return "/masterpage_sys/member/companySaveForm";
    }

    /* 회원관리 > 회원정보관리 > LMS관리자 등록 */
    @GetMapping("/admin/saveForm")
    public String adminSaveForm(Model model) {

        return "/masterpage_sys/member/adminSaveForm";
    }


    /**
     * 회원관리 - 리스트
     */
    /* 회원관리 > 회원정보관리 > 수강생 리스트 */
    @GetMapping("/student/")
    public String studentListForm(Model model) {
        return "/masterpage_sys/member/studentListForm";
    }

    /* 회원관리 > 회원정보관리 > 교강사(튜터) 리스트 */
    @GetMapping("/teacher/")
    public String teacherListForm(Model model) {


        return "/masterpage_sys/member/teacherListForm";
    }

    /* 회원관리 > 회원정보관리 > 위탁기업 리스트 */
    @GetMapping("/company/")
    public String companyListForm(Model model) {


        return "/masterpage_sys/member/companyListForm";
    }

    /* 회원관리 > 회원정보관리 > LMS관리자 리스트 */
    @GetMapping("/admin/")
    public String adminListForm(Model model) {


        return "/masterpage_sys/member/adminListForm";
    }
}
