package com.example.junguniv_bb._core.exception;

public enum ValidExceptionMessage {
    INVALID_USERID(Message.INVALID_USERID),
    INVALID_PASSWORD(Message.INVALID_PASSWORD),
    INVALID_NAME(Message.INVALID_NAME),
    INVALID_EMAIL(Message.INVALID_EMAIL),
    INVALID_BIRTHDAY(Message.INVALID_BIRTHDAY),
    INVALID_SEX("성별은 'M' 또는 'F'로 입력해야 합니다."),
    INVALID_TELMOBILE(Message.INVALID_TELMOBILE),
    EMPTY_CONTENT(Message.EMPTY_CONTENT),
    EMPTY_POINT(Message.EMPTY_POINT),
    IS_CORRECT(Message.IS_CORRECT),
    INVALID_DATE_FORMAT(Message.INVALID_DATE_FORMAT); // 추가된 항목

    final private String message;

    ValidExceptionMessage(String message) {
        this.message = message;
    }

    public static class Message {
        public static final String INVALID_USERID = "아이디는 4에서 15자 이내여야 합니다.";
        public static final String INVALID_PASSWORD = "패스워드는 4에서 20자 이내여야 합니다.";
        public static final String INVALID_NAME = "이름은 15자 이내여야 합니다.";
        public static final String INVALID_EMAIL = "이메일 형식이 올바르지 않습니다.";
        public static final String INVALID_BIRTHDAY = "생년월일을 입력하세요.";
        public static final String INVALID_SEX = "성별은 'M' 또는 'F'로 입력해야 합니다."; // 추가된 메시지
        public static final String INVALID_TELMOBILE = "휴대폰 번호를 입력하세요.";
        public static final String EMPTY_CONTENT = "내용을 입력하세요.";
        public static final String EMPTY_POINT = "배점을 입력하세요.";
        public static final String EMPTY_EXAM_RESULT = "평가를 입력하세요.";
        public static final String EMPTY_QUESTION = "문제를 입력하세요.";
        public static final String EMPTY_QUESTION_ANSWER = "답안을 입력하세요.";
        public static final String IS_CORRECT = "정답 여부를 입력하세요.";
        public static final String INVALID_DATE_FORMAT = "날짜 형식이 잘못되었습니다. 올바른 형식: yyyy-MM-dd"; // 추가된 메시지
    }
}

