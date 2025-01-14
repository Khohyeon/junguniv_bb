package com.example.junguniv_bb._core.exception;

import com.example.junguniv_bb._core.util.APIUtils;
import org.springframework.http.HttpStatus;

public class Exception500 extends RuntimeException {
    public Exception500(String message) {
        super(message);
    }

    public APIUtils.APIResult<?> body() {
        return APIUtils.error(getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
