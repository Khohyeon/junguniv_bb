package com.example.junguniv_bb.domain.subject.service;

import com.example.junguniv_bb.domain.subject.model.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
@RequestMapping("/masterpage_pro/subject/api")
public class SubjectService {

    private final SubjectRepository subjectRepository;

}
