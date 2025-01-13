package com.example.junguniv_bb.domain.member._enum;

/**
 * 유저의 권한을 나타내는 enum 타입입니다.
 *
 * <ul>
 *     <li>{@link #ADMIN}: 서버관리자를 의미합니다.</li>
 *     <li>{@link #STUDENT}: 직원을 의미합니다.</li>
 *     <li>{@link #TEACHER}: 강사를 의미합니다.</li>
 *     <li>{@link #GUEST}: 역할을 할당받지 않은 게스트를 의미합니다.</li>
 * </ul>
 */
public enum UserType {
    /**
     * 관리자를 의미합니다.
     */
    ADMIN("관리자"),

    /**
     * 신입생(학생)을 의미합니다.
     */
    STUDENT("신입생"),

    /**
     * 재학생(학생)을 의미합니다.
     */
    JAEHAK("재학생"),

    /**
     * 졸업생(학생)을 의미합니다.
     */
    GRADUATE("졸업생"),

    /**
     * 강사를 의미합니다.
     */
    TEACHER("강사"),

    /**
     * 역할을 할당받지 않은 게스트를 의미합니다.
     */
    GUEST("게스트");

    final private String text;

    UserType(String message) {
        this.text = message;
    }

    public String getText() {
        return text;
    }
}

