package com.example.junguniv_bb.domain.agreement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/agreement")
public class AgreementController {

    @GetMapping("/joinForm")
    public String joinForm() {
        return "masterpage_sys/agreement/joinForm";
    }

    @GetMapping("/courseForm")
    public String courseForm() {
        return "masterpage_sys/agreement/courseForm";
    }
}
