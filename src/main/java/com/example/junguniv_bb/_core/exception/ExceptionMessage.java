package com.example.junguniv_bb._core.exception;

public enum ExceptionMessage {
    COMMON_FORBIDDEN("해당 기능을 사용할 권한이 없습니다."),
    NOT_FOUND_ABILITY_UNIT("존재하지 않는 능력 단위입니다."),
    INVALID_TYPE("존재하지 않는 역할입니다.: "),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    IS_NOT_REFRESH_TOKEN("code5: refreshToken이 아닙니다."),
    EXPIRED_TOKEN("토큰 유효 기간이 만료되었습니다."),
    LOGIN_FAIL("비밀번호가 틀렸습니다."),
    NOT_FOUND_ID("아이디가 틀렸습니다."),
    NOT_FOUND_AGREEMENT("존재하지 않는 약관입니다."),
    NOT_FOUND_MEMBER("존재하지 않는 멤버입니다."),
    NOT_FOUND_BBS("존재하지 않는 게시글입니다."),
    NOT_FOUND_BBS_FILE("존재하지 않는 게시글 첨부파일입니다."),
    NOT_FOUND_MANAGER_MENU("존재하지 않는 메뉴입니다."),
    NOT_FOUND_PARENT_MANAGER_MENU("존재하지 않는 부모 메뉴입니다."),
    NOT_FOUND_AUTH_LEVEL("존재하지 않는 권한 레벨입니다."),
    NOT_FOUND_MANAGER_AUTH("존재하지 않는 메뉴 권한입니다."),
    NOT_FOUND_BBS_GROUP("존재하지 않는 게시판입니다."),
    NOT_FOUND_REFUND_PRICE("존재하지 않는 지원금입니다."),
    NOT_FOUND_COLLEGE("존재하지 않는 분야입니다."),
    NOT_FOUND_MAJOR("존재하지 않는 분류입니다."),
    EXISTS_AUTH_LEVEL("이미 존재하는 권한 레벨입니다."),
    EXISTS_LINKED_MEMBER_AUTH_LEVEL("연결된 관리자가 있는 관리자 권한은 삭제할 수 없습니다."),
    NOT_FOUND_SYSTEM_CODE("존재하지 않는 시스템 코드입니다."),

    JOIN_FAIL("code17: 회원가입 중 오류가 발생했습니다."),
    EXISTS_EXAM_PAPER_IN_EXAM("code18: 시험안에 시험지가 존재합니다."),
    NOT_FOUND_EXAM_RESULT("code19: 존재하지 않는 평가 결과 입니다."),
    NOT_FOUND_QUESTION_ANSWER("code20: 존재하지 않는 문제 답안 입니다."),
    INVALID_INPUT_VALUE("유효하지 않은 입력값입니다.");

    final private String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
