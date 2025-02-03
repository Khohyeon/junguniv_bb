package com.example.junguniv_bb.domain.major.service;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb.domain.college.model.College;
import com.example.junguniv_bb.domain.college.model.CollegeRepository;
import com.example.junguniv_bb.domain.major.model.Major;
import com.example.junguniv_bb.domain.major.model.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorService {

    private final MajorRepository majorRepository;
    private final CollegeRepository collegeRepository;

    public List<Major> getAllMajors() {
        return majorRepository.findAll();
    }

    public List<Major> getAllMajorsByCollege(Long collegeIdx) {
        College college = collegeRepository.findById(collegeIdx)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_COLLEGE.getMessage()));
        return majorRepository.findAllByCollege(college);
    }
}
