package com.example.junguniv_bb.domain.counsel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys_sys/counsel")
public class CounselController {

    @GetMapping("/listForm")
    public String listForm() {
        return "masterpage_sys_sys/counsel/listForm";
    }

    @GetMapping("/saveForm")
    public String saveForm() {
        return "masterpage_sys_sys/counsel/saveForm";
    }

}
