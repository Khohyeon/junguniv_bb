package com.example.junguniv_bb.domain.managermenu._enum;

/**
 * 메뉴의 그룹을 나타내는 enum 타입입니다.
 */
public enum MenuType {
    ADMIN_REFUND("관리자(환급)"),
    ADMIN_NORMAL("관리자(일반)"),
    TEACHER("교강사"),
    COMPANY("위탁기업"),
    SYSTEM("시스템"),
    STUDENT("학생");

    final private String text;

    MenuType(String message) {
        this.text = message;
    }

    public String getText() {
        return text;
    }
}
