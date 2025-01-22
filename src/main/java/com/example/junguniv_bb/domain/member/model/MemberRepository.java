package com.example.junguniv_bb.domain.member.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Meta;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.junguniv_bb.domain.member._enum.UserType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Meta(comment = "특정 authLevel을 사용하는 모든 회원의 authLevel을 null로 설정합니다.")
    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.authLevel = null WHERE m.authLevel = :authLevel")
    int setAuthLevelToNullForMembers(@Param("authLevel") Long authLevel);

    @Meta(comment = "회원 아이디로 조회")
    Optional<Member> findByUserId(String userId);

    @Meta(comment = "회원 유형으로 조회")
    Page<Member> findByUserType(UserType userType, Pageable pageable);

    @Meta(comment = "회원관리 - 학생 회원 검색")
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

    @Meta(comment = "회원관리 - 교강사 회원 검색")
    @Query("SELECT m FROM Member m " +
           "WHERE m.userType = 'TEACHER' " +
           "AND (:name IS NULL OR m.name LIKE CONCAT('%', :name, '%')) " +
           "AND (:userId IS NULL OR m.userId LIKE CONCAT('%', :userId, '%')) " +
           "AND (:jobEmployeeType IS NULL OR m.jobEmployeeType = :jobEmployeeType) " +
           "AND (:telMobile IS NULL OR m.telMobile LIKE CONCAT('%', :telMobile, '%')) " +
           "AND (:email IS NULL OR m.email LIKE CONCAT('%', :email, '%'))")
    Page<Member> searchTeachers(@Param("name") String name,
                              @Param("userId") String userId,
                              @Param("jobEmployeeType") String jobEmployeeType,
                              @Param("telMobile") String telMobile,
                              @Param("email") String email,
                              Pageable pageable);

    @Meta(comment = "회원관리 - 기업 회원 검색")
    @Query("SELECT m FROM Member m " +
           "WHERE m.userType = 'COMPANY' " +
           "AND (:jobName IS NULL OR m.jobName LIKE CONCAT('%', :jobName, '%')) " +
           "AND (:userId IS NULL OR m.userId LIKE CONCAT('%', :userId, '%')) " +
           "AND (:jobNumber IS NULL OR m.jobNumber LIKE CONCAT('%', :jobNumber, '%')) " +
           "AND (:contractorName IS NULL OR m.contractorName LIKE CONCAT('%', :contractorName, '%')) " +
           "AND (:contractorTel IS NULL OR m.contractorTel LIKE CONCAT('%', :contractorTel, '%')) " +
           "AND (:contractorEtc IS NULL OR m.contractorEtc LIKE CONCAT('%', :contractorEtc, '%')) " +
           "AND (:jobScale IS NULL OR m.jobScale = :jobScale)")
    Page<Member> searchCompanies(@Param("jobName") String jobName,
                               @Param("userId") String userId,
                               @Param("jobNumber") String jobNumber,
                               @Param("contractorName") String contractorName,
                               @Param("contractorTel") String contractorTel,
                               @Param("contractorEtc") String contractorEtc,
                               @Param("jobScale") String jobScale,
                               Pageable pageable);

    @Meta(comment = "회원관리 - 관리자 회원 검색")
    @Query("SELECT m FROM Member m " +
           "WHERE m.userType = 'ADMIN' " +
           "AND (:name IS NULL OR m.name LIKE CONCAT('%', :name, '%')) " +
           "AND (:userId IS NULL OR m.userId LIKE CONCAT('%', :userId, '%')) " +
           "AND (:jobCourseDuty IS NULL OR m.jobCourseDuty = :jobCourseDuty)")
    Page<Member> searchAdmins(@Param("name") String name,
                            @Param("userId") String userId,
                            @Param("jobCourseDuty") String jobCourseDuty,
                            Pageable pageable);

    /* 주소록 검색 */
    @Query("SELECT m FROM Member m WHERE " +
           "(:name IS NULL OR m.name LIKE %:name%) AND " +
           "(:userId IS NULL OR m.userId LIKE %:userId%) AND " +
           "(:address IS NULL OR m.addr1 LIKE %:address% OR m.addr2 LIKE %:address%) AND " +
           "(:telMobile IS NULL OR m.telMobile LIKE %:telMobile%) AND " +
           "(:email IS NULL OR m.email LIKE %:email%) AND " +
           "(:jobName IS NULL OR m.jobName LIKE %:jobName%)")
    Page<Member> searchAddress(
            @Param("name") String name,
            @Param("userId") String userId,
            @Param("address") String address,
            @Param("telMobile") String telMobile,
            @Param("email") String email,
            @Param("jobName") String jobName,
            Pageable pageable);

    List<Member> findByNameContainingIgnoreCase(String name);


    @Meta(comment = "각 AuthLevel 별 회원 수를 조회합니다.")
    @Query("SELECT m.authLevel, COUNT(m) FROM Member m WHERE m.authLevel IN :authLevels GROUP BY m.authLevel")
    List<Object[]> countMembersByAuthLevelRaw(@Param("authLevels") List<Long> authLevels);

    default Map<Long, Long> countMembersByAuthLevel(List<Long> authLevels) {
        List<Object[]> results = countMembersByAuthLevelRaw(authLevels);
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],  // authLevel
                        row -> (Long) row[1]   // count
                ));
    }

    @Meta(comment = "기존 authLevel 값을 새로운 authLevel 값으로 업데이트합니다.")
    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.authLevel = :newAuthLevel WHERE m.authLevel = :oldAuthLevel")
    int updateAuthLevelForMembers(@Param("oldAuthLevel") Long oldAuthLevel, @Param("newAuthLevel") Long newAuthLevel);
}
