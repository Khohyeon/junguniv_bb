package com.example.junguniv_bb._core.security;

import com.example.junguniv_bb._core.util.RequestUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        if (RequestUtils.isApiRequest(request)) {
            // API 요청인 경우 JSON 응답 반환
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter()
                    .write("{\"type\" : \"about:blank\", \"title\" : \"FORBIDDEN\","
                            + "\"status\" : 403 ,"
                            + "\"detail\" : \"" + accessDeniedException.getMessage() + "\","
                            + "\"instance\" : \"" + request.getServletPath() + "\""
                            + "}");
        } else {
            // 웹 요청인 경우 커스텀 접근 거부 페이지로 리다이렉트
            response.sendRedirect("/access-denied");
        }
    }
}


// response.setStatus(HttpStatus.FORBIDDEN.value());
// response.setContentType("application/json");
// response.setCharacterEncoding("UTF-8");
// response.getWriter().write(
//         "{\"type\" : \"about:blank\", \"title\" : \"FORBIDDEN\","
//                 + "\"status\" : 403 ,"
//                 + "\"detail\" : \"" + accessDeniedException.getMessage() + "\","
//                 + "\"instance\" : \"" + request.getServletPath() + "\","
//                 + "}"
// );