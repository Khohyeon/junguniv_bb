package com.example.junguniv_bb._core.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.AntPathMatcher;

/**
 * Request 요청이 API 요청인지 웹 요청인지 확인하는 유틸 클래스
 */
public class RequestUtils {
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public static boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        
        // Form 요청은 API가 아닌 것으로 처리
        if (uri.contains("Form")) {
            return false;
        }

        // TODO BB 시스템에 맞게 엔드포인트 수정 필요
        return antPathMatcher.match("/nGmaster/**/api/**", uri) ||
                antPathMatcher.match("/nGsmart/**/api/**", uri) ||
               antPathMatcher.match("/api/**", uri) ||
               (request.getHeader("Accept") != null && 
                request.getHeader("Accept").contains("application/json")) ||
               (request.getHeader("Content-Type") != null && 
                request.getHeader("Content-Type").contains("application/json"));
    }
}