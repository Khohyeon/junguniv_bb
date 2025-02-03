package com.example.junguniv_bb.domain.college.service;

import com.example.junguniv_bb.domain.college.model.College;
import com.example.junguniv_bb.domain.college.model.CollegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollegeService {

    private final CollegeRepository collegeRepository;

    public List<College> getAllColleges() {
        return collegeRepository.findAll();
    }
}
