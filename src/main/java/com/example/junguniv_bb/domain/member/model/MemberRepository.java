package com.example.junguniv_bb.domain.member.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Meta;

import com.example.junguniv_bb.domain.member._enum.UserType;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Meta(comment = "회원 아이디로 조회")
    Optional<Member> findByUserId(String userId);

    @Meta(comment = "회원 유형으로 조회")
    Page<Member> findByUserType(UserType userType, Pageable pageable);
}
