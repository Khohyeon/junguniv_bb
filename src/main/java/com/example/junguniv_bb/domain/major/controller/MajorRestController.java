package com.example.junguniv_bb.domain.major.controller;

import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.major.model.Major;
import com.example.junguniv_bb.domain.major.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage_pro/major/api")
public class MajorRestController {

    private final MajorService majorService;

    @GetMapping("/{collegeIdx}")
    public ResponseEntity<?> majorSearch(@PathVariable("collegeIdx") Long collegeIdx) {

        List<Major> allMajorsByCollege = majorService.getAllMajorsByCollege(collegeIdx);
        return ResponseEntity.ok(APIUtils.success(allMajorsByCollege));
    }
}
