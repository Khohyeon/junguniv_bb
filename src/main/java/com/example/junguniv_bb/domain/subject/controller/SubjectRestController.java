package com.example.junguniv_bb.domain.subject.controller;

import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.subject.dto.SubjectSaveReqDTO;
import com.example.junguniv_bb.domain.subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage_pro/subject/api")
public class SubjectRestController {

    private final SubjectService subjectService;

    @PostMapping("/save")
    public ResponseEntity<?> subjectSave(
            @RequestBody SubjectSaveReqDTO subjectSaveReqDTO
    ) {



        return ResponseEntity.ok(APIUtils.success(null));
    }
}
