package com.example.junguniv_bb.domain.major.model;

import com.example.junguniv_bb.domain.college.model.College;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MajorRepository extends JpaRepository<Major, Long> {
    List<Major> findAllByCollege(College college);
}
