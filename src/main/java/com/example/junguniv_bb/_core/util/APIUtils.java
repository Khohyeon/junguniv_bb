package com.example.junguniv_bb._core.util;

import org.springframework.http.HttpStatus;

public class APIUtils {

    public static <T> APIResult<T> success(T response) {
        return new APIResult<>(true, response, null);
    }

    public static APIResult<?> error(String message, HttpStatus status) {
        return new APIResult<>(false, null, new APIError(message, false, status.value()));
    }

    public static APIResult<?> errorConfirm(String message, boolean useConfirm, HttpStatus status) {
        return new APIResult<>(false, null, new APIError(message, useConfirm, status.value()));
    }

    public record APIResult<T>(boolean success, T response, APIError error) {
    }

    public record APIError(String message, boolean useConfirm, int status) {
    }
}
