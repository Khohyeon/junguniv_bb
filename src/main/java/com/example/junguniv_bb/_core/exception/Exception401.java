package com.example.junguniv_bb._core.exception;

import com.example.junguniv_bb._core.util.APIUtils;
import org.springframework.http.HttpStatus;

public class Exception401 extends RuntimeException {
    public Exception401(String message) {
        super(message);
    }

    public APIUtils.APIResult<?> body() {
        return APIUtils.error(getMessage(), HttpStatus.UNAUTHORIZED);
    }

    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}
