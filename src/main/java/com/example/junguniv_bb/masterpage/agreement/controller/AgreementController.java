package com.example.junguniv_bb.masterpage.agreement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage/agreement")
public class AgreementController {

    @GetMapping("/joinForm")
    public String joinForm() {
        return "masterpage/agreement/joinForm";
    }

    @GetMapping("/courseForm")
    public String courseForm() {
        return "masterpage/agreement/courseForm";
    }
}
