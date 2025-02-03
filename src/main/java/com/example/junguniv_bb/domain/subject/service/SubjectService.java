package com.example.junguniv_bb.domain.subject.service;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb.domain.college.model.College;
import com.example.junguniv_bb.domain.college.model.CollegeRepository;
import com.example.junguniv_bb.domain.major.model.Major;
import com.example.junguniv_bb.domain.major.model.MajorRepository;
import com.example.junguniv_bb.domain.subject.dto.SubjectSaveReqDTO;
import com.example.junguniv_bb.domain.subject.dto.SubjectStep1SaveReqDTO;
import com.example.junguniv_bb.domain.subject.dto.SubjectStep2SaveReqDTO;
import com.example.junguniv_bb.domain.subject.dto.SubjectStep3SaveReqDTO;
import com.example.junguniv_bb.domain.subject.model.Subject;
import com.example.junguniv_bb.domain.subject.model.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
@RequestMapping("/masterpage_pro/subject/api")
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final CollegeRepository collegeRepository;
    private final MajorRepository majorRepository;

    @Transactional
    public Subject save1(SubjectStep1SaveReqDTO subjectStep1SaveReqDTO) {

        College college = collegeRepository.findById(subjectStep1SaveReqDTO.collegeIdx())
                        .orElseThrow(()-> new Exception400(ExceptionMessage.NOT_FOUND_COLLEGE.getMessage()));

        Major major = majorRepository.findById(subjectStep1SaveReqDTO.majorIdx())
                .orElseThrow(()-> new Exception400(ExceptionMessage.NOT_FOUND_MAJOR.getMessage()));

        return subjectRepository.save(subjectStep1SaveReqDTO.saveEntity(college, major));
    }

    @Transactional
    public void save2(SubjectStep2SaveReqDTO subjectStep2SaveReqDTO) {
        subjectRepository.save(subjectStep2SaveReqDTO.step2Save());
    }

    @Transactional
    public void save3(SubjectStep3SaveReqDTO subjectStep3SaveReqDTO) {
        subjectRepository.save(subjectStep3SaveReqDTO.step3Save());
    }
}
