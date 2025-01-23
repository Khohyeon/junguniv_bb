package com.example.junguniv_bb.domain._auth.dto;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ValidExceptionMessage;
import com.example.junguniv_bb.domain.member._enum.UserType;
import com.example.junguniv_bb.domain.member.model.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public record AuthJoinReqDTO(

        @NotEmpty(message = ValidExceptionMessage.Message.INVALID_USERID)
        @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하로 입력하세요.")
        String userId,

        // 영문 대/소문자, 특수문자, 숫자 조합으로 10자 이상
        @NotEmpty(message = ValidExceptionMessage.Message.INVALID_PASSWORD)
        @Size(min = 10, message = "비밀번호는 최소 10자 이상이어야 합니다.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,}$",
                message = "비밀번호는 영문 대/소문자, 숫자, 특수문자를 포함하여 10자 이상이어야 합니다."
        )
        String pwd,

        @NotEmpty(message = ValidExceptionMessage.Message.INVALID_BIRTHDAY)
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "생년월일은 'YYYY-MM-DD' 형식이어야 합니다.")
        String birthDate,

        @NotEmpty(message = ValidExceptionMessage.Message.INVALID_SEX)
        @Pattern(regexp = "^[MF]$", message = ValidExceptionMessage.Message.INVALID_SEX)
        String sex,

        @NotEmpty(message = ValidExceptionMessage.Message.INVALID_TELMOBILE)
        @Pattern(regexp = "\\d{3}-\\d{3,4}-\\d{4}", message = "전화번호는 '000-0000-0000' 형식이어야 합니다.")
        String telMobile,

        @NotEmpty(message = ValidExceptionMessage.Message.INVALID_EMAIL)
        @Email(message = "유효한 이메일 형식이어야 합니다.")
        String email,

        String zipcode,
        String addr1,
        String addr2,
        String etc,
        String userType,
        String name,
        String agreePage, // 회원가입약관내용
        LocalDateTime agreeDate // 약관동의최종날자
) {

    public Member toEntity(String pwd) {
        return Member.builder()
                .userId(userId)
                .pwd(pwd)
                // .birthDay(birthDate)
                .sex(sex)
                .telMobile(telMobile)
                .email(email)
                // .zipCode(zipcode)
                .addr1(addr1)
                .addr2(addr2)
                // .etc(etc)
                .userType(UserType.STUDENT)
                // .agreePage(agreePage)
                // .agreeDate(agreeDate)
                .memberState("Y")
                .name(name)
                .jumin(formatBirthDate(birthDate, sex))
                .build();

    }

    // 생성자에서 나이 검증 로직 추가
    public AuthJoinReqDTO {
        // 생년월일을 LocalDate로 파싱
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDateParsed = LocalDate.parse(birthDate, formatter);
        LocalDate currentDate = LocalDate.now();

        // 나이 계산
        int age = Period.between(birthDateParsed, currentDate).getYears();

        // 나이가 20세 미만이면 예외 발생
        if (age < 18) {
            throw new Exception400("가입은 만 19세 이상만 가능합니다.");
        }
    }


    private static String formatBirthDate(String birthDate, String sex) {
        // 날짜 변환
        LocalDate date = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyMMdd"));

        // 성별 코드 설정
        int year = date.getYear();
        String genderCode;
        if ("M".equalsIgnoreCase(sex)) {
            genderCode = (year < 2000) ? "1" : "3";
        } else if ("F".equalsIgnoreCase(sex)) {
            genderCode = (year < 2000) ? "2" : "4";
        } else {
            throw new IllegalArgumentException("Invalid gender: " + sex);
        }

        // 결과 반환
        return formattedDate + "-" + genderCode;
    }

    // 나이를 계산하는 메소드
    public void validateAge() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDateParsed = LocalDate.parse(birthDate, formatter);
        LocalDate currentDate = LocalDate.now();

        // 나이 계산
        int age = Period.between(birthDateParsed, currentDate).getYears();

        // 20살 이하인 경우 예외 발생
        if (age <= 20) {
            throw new IllegalArgumentException("20살 이하의 사용자는 가입할 수 없습니다.");
        }
    }
}
