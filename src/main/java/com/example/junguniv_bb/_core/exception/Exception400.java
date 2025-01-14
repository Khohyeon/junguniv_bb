package com.example.junguniv_bb._core.exception;

import com.example.junguniv_bb._core.util.APIUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Exception400 extends RuntimeException {

    private final String message;
    private final boolean useConfirm;

    // 기본 생성자 (useConfirm은 false)
    public Exception400(String message) {
        super(message);
        this.message = message;
        this.useConfirm = false;
    }

    // useConfirm을 설정할 수 있는 생성자
    public Exception400(String message, boolean useConfirm) {
        super(message);
        this.message = message;
        this.useConfirm = useConfirm;
    }

    // 일반 에러 반환
    public APIUtils.APIResult<?> body() {
        return APIUtils.error(getMessage(), HttpStatus.BAD_REQUEST);
    }

    // confirm 에러 반환
    public APIUtils.APIResult<?> bodyWithConfirm() {
        return APIUtils.errorConfirm(getMessage(), isUseConfirm(), HttpStatus.BAD_REQUEST);
    }

    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }
}
