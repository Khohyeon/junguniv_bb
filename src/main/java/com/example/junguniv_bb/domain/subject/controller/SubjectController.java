package com.example.junguniv_bb.domain.subject.controller;

import com.example.junguniv_bb.domain.college.service.CollegeService;
import com.example.junguniv_bb.domain.major.service.MajorService;
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
    private final CollegeService collegeService;
    private final MajorService majorService;

    @GetMapping
    public String subjectPage(Model model) {
        return "/masterpage_pro/subject/listForm";
    }

    @GetMapping("/save")
    public String subjectSaveForm(Model model) {
        model.addAttribute("collegeList", collegeService.getAllColleges());
        model.addAttribute("majorList", majorService.getAllMajors());
        return "/masterpage_pro/subject/saveForm";
    }
}
