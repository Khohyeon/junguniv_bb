package com.example.junguniv_bb.masterpage.counsel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage/counsel")
public class CounselController {

    @GetMapping("/listForm")
    public String listForm() {
        return "masterpage/counsel/listForm";
    }

    @GetMapping("/saveForm")
    public String saveForm() {
        return "masterpage/counsel/saveForm";
    }

}
