package com.example.junguniv_bb.domain.subject.controller;

import com.example.junguniv_bb.domain.subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage_pro/subject")
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public String subjectPage(Model model) {
        return "/masterpage_pro/subject/listForm";
    }

    @GetMapping("/save")
    public String subjectSaveForm(Model model) {
        return "/masterpage_pro/subject/saveForm";
    }
}
