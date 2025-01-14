package com.example.junguniv_bb.domain.member._enum;

/**
 * 유저의 권한을 나타내는 enum 타입입니다.
 */
public enum UserType {

    ADMIN("관리자"), // 웹 관리자
    STUDENT("수강생"),
    TEACHER("교강사(튜터)"),
    COMPANY("위탁기업"),
    MANAGER("LMS관리자"),
    GUEST("게스트"); // 역할을 할당받지 않은 게스트

    final private String text;

    UserType(String message) {
        this.text = message;
    }

    public String getText() {
        return text;
    }
}

