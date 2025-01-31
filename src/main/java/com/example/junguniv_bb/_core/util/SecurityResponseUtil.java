package com.example.junguniv_bb._core.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class SecurityResponseUtil {
    
    public static void writeJsonResponse(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpStatus status,
            String message
    ) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"type\" : \"about:blank\", "
                + "\"title\" : \"" + status.getReasonPhrase() + "\","
                + "\"status\" : " + status.value() + ","
                + "\"detail\" : \"" + message + "\","
                + "\"instance\" : \"" + request.getServletPath() + "\""
                + "}"
        );
    }
} 