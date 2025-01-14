package com.example.junguniv_bb._core.exception;

import org.springframework.http.HttpStatus;
import com.example.junguniv_bb._core.util.APIUtils;

public class Exception403 extends RuntimeException {
    public Exception403(String message) {
        super(message);
    }

    public APIUtils.APIResult<?> body(){
        return APIUtils.error(getMessage(), HttpStatus.FORBIDDEN);
    }

    public HttpStatus status(){
        return HttpStatus.FORBIDDEN;
    }
}
