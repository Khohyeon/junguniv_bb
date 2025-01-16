package com.example.junguniv_bb.domain.member.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Meta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.junguniv_bb.domain.member._enum.UserType;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Meta(comment = "회원 아이디로 조회")
    Optional<Member> findByUserId(String userId);

    @Meta(comment = "회원 유형으로 조회")
    Page<Member> findByUserType(UserType userType, Pageable pageable);

    @Meta(comment = "학생 회원 검색")
    @Query("SELECT m FROM Member m " +
           "WHERE m.userType = 'STUDENT' " +
           "AND (:name IS NULL OR m.name LIKE CONCAT('%', :name, '%')) " +
           "AND (:userId IS NULL OR m.userId LIKE CONCAT('%', :userId, '%')) " +
           "AND (:birthday IS NULL OR m.birthday = :birthday) " +
           "AND (:telMobile IS NULL OR m.telMobile LIKE CONCAT('%', :telMobile, '%')) " +
           "AND (:email IS NULL OR m.email LIKE CONCAT('%', :email, '%')) " +
           "AND (:chkDormant IS NULL OR m.chkDormant = :chkDormant) " +
           "AND (:loginPass IS NULL OR m.loginPass = :loginPass) " +
           "AND (:chkForeigner IS NULL OR m.chkForeigner = :chkForeigner) " +
           "AND (:sex IS NULL OR m.sex = :sex) " +
           "AND (:jobName IS NULL OR m.jobName LIKE CONCAT('%', :jobName, '%')) " +
           "AND (:jobWorkState IS NULL OR m.jobWorkState = :jobWorkState) " +
           "AND (:jobDept IS NULL OR m.jobDept LIKE CONCAT('%', :jobDept, '%')) " +
           "AND (:chkSmsReceive IS NULL OR m.chkSmsReceive = :chkSmsReceive) " +
           "AND (:chkMailReceive IS NULL OR m.chkMailReceive = :chkMailReceive) " +
           "AND (:chkIdentityVerification IS NULL OR m.chkIdentityVerification = :chkIdentityVerification) " +
           "AND (:loginClientIp IS NULL OR m.loginClientIp LIKE CONCAT('%', :loginClientIp, '%'))")
    Page<Member> searchStudents(@Param("name") String name,
                              @Param("userId") String userId,
                              @Param("birthday") String birthday,
                              @Param("telMobile") String telMobile,
                              @Param("email") String email,
                              @Param("chkDormant") String chkDormant,
                              @Param("loginPass") String loginPass,
                              @Param("chkForeigner") String chkForeigner,
                              @Param("sex") String sex,
                              @Param("jobName") String jobName,
                              @Param("jobWorkState") String jobWorkState,
                              @Param("jobDept") String jobDept,
                              @Param("chkSmsReceive") String chkSmsReceive,
                              @Param("chkMailReceive") String chkMailReceive,
                              @Param("chkIdentityVerification") String chkIdentityVerification,
                              @Param("loginClientIp") String loginClientIp,
                              Pageable pageable);
}
