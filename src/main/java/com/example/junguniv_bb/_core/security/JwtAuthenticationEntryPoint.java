package com.example.junguniv_bb._core.security;

import com.example.junguniv_bb._core.util.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        
        String errorMessage = authException.getMessage();
        
        // 계정 중지 상태인 경우 리다이렉트 (웹 요청일 때만)
        if (errorMessage != null && errorMessage.startsWith("/") && !RequestUtils.isApiRequest(request)) {
            response.sendRedirect(errorMessage);
            return;
        }

        /* JSON 응답 - API 요청일 경우 */
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"type\" : \"about:blank\", \"title\" : \"Unauthorized\","
                        + "\"status\" : 401 ,"
                        + "\"detail\" : \"" + (errorMessage != null && errorMessage.startsWith("/") ? 
                            "계정이 정지되었습니다. 관리자에게 문의하세요." : authException.getMessage()) + "\","
                        + "\"instance\" : \"" + request.getServletPath() + "\""
                        + "}"
        );
    }
}