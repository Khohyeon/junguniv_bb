package com.example.junguniv_bb._core.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.AntPathMatcher;
import lombok.extern.slf4j.Slf4j;

/**
 * Request 요청이 API 요청인지 웹 요청인지 확인하는 유틸 클래스
 */
@Slf4j
public class RequestUtils {
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public static boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        
        // Form 요청은 API가 아닌 것으로 처리
        if (uri.contains("Form")) {
            return false;
        }

        // masterpage_sys 하위의 api 요청 패턴 매칭
        boolean isApi = uri.contains("/api/") || uri.endsWith("/api");
        log.info("[권한 체크] {} {} - API 요청: {}", method, uri, isApi);
        return isApi;
    }

    /**
     * HTTP 메소드를 기반으로 CRUD 작업을 판별
     */
    public static String getCrudOperation(HttpServletRequest request) {
        String method = request.getMethod();
        
        return switch (method.toUpperCase()) {
            case "GET" -> "READ";
            case "POST" -> "WRITE";
            case "PUT", "PATCH" -> "MODIFY";
            case "DELETE" -> "DELETE";
            default -> "UNKNOWN";
        };
    }
}