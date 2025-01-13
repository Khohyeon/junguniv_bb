package com.example.junguniv_bb.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage/member")
@Slf4j
public class MemberController {

    @GetMapping("/")
    public String listForm(Model model) {


        return "/masterpage/member/listForm";
    }
}
