package com.example.junguniv_bb._core.security;

import com.example.junguniv_bb._core.exception.Exception400;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;

/**
 * 버퍼 오버플로우 공격을 방지하기 위한 인터셉터입니다.
 * 요청의 크기, 파라미터 길이, 헤더 길이 등을 제한합니다.
 */
@Component
public class BufferOverflowInterceptor implements HandlerInterceptor {
    
    private static final Logger log = LoggerFactory.getLogger(BufferOverflowInterceptor.class);

    // 최대 허용 크기 상수 정의
    private static final int MAX_REQUEST_SIZE = 2 * 1024 * 1024;  // 2MB
    private static final int MAX_PARAMETER_LENGTH = 1000;  // 1000자
    private static final int MAX_HEADER_LENGTH = 1000;    // 1000자
    private static final int MAX_PARAMETER_COUNT = 100;   // 최대 100개 파라미터
    private static final int MAX_HEADER_COUNT = 50;       // 최대 50개 헤더

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            // 요청 크기 검증
            String contentLengthHeader = request.getHeader("Content-Length");
            if (contentLengthHeader != null) {
                long contentLength = Long.parseLong(contentLengthHeader);
                if (contentLength > MAX_REQUEST_SIZE) {
                    log.warn("Request size exceeds maximum allowed size: {} bytes", contentLength);
                    throw new Exception400("요청 크기가 너무 큽니다. 최대 허용 크기: 2MB");
                }
            }

            // 파라미터 개수 및 길이 검증
            validateParameters(request);

            // 헤더 개수 및 길이 검증
            validateHeaders(request);

            // URL 길이 검증
            String requestURL = request.getRequestURL().toString();
            if (requestURL.length() > MAX_PARAMETER_LENGTH) {
                log.warn("URL length exceeds maximum allowed length: {} characters", requestURL.length());
                throw new Exception400("URL 길이가 너무 깁니다.");
            }

            return true;
        } catch (NumberFormatException e) {
            log.error("Invalid Content-Length header", e);
            throw new Exception400("잘못된 요청 형식입니다.");
        } catch (Exception e) {
            log.error("Error in buffer overflow validation", e);
            throw new Exception400("요청 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 요청 파라미터의 개수와 길이를 검증합니다.
     */
    private void validateParameters(HttpServletRequest request) {
        // 파라미터 개수 검증
        int parameterCount = request.getParameterMap().size();
        if (parameterCount > MAX_PARAMETER_COUNT) {
            log.warn("Parameter count exceeds maximum allowed: {}", parameterCount);
            throw new Exception400("파라미터 개수가 너무 많습니다. 최대 허용 개수: " + MAX_PARAMETER_COUNT);
        }

        // 각 파라미터 값의 길이 검증
        request.getParameterMap().forEach((key, values) -> {
            for (String value : values) {
                if (value != null && value.length() > MAX_PARAMETER_LENGTH) {
                    log.warn("Parameter length exceeds maximum allowed for parameter {}: {} characters", key, value.length());
                    throw new Exception400("파라미터 '" + key + "'의 길이가 너무 깁니다. 최대 허용 길이: " + MAX_PARAMETER_LENGTH);
                }
            }
        });
    }

    /**
     * 요청 헤더의 개수와 길이를 검증합니다.
     */
    private void validateHeaders(HttpServletRequest request) {
        // 헤더 개수 검증
        int headerCount = 0;
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            headerCount++;
            if (headerCount > MAX_HEADER_COUNT) {
                log.warn("Header count exceeds maximum allowed: {}", headerCount);
                throw new Exception400("헤더 개수가 너무 많습니다. 최대 허용 개수: " + MAX_HEADER_COUNT);
            }

            // 각 헤더 값의 길이 검증
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            if (headerValue != null && headerValue.length() > MAX_HEADER_LENGTH) {
                log.warn("Header length exceeds maximum allowed for header {}: {} characters", headerName, headerValue.length());
                throw new Exception400("헤더 '" + headerName + "'의 길이가 너무 깁니다. 최대 허용 길이: " + MAX_HEADER_LENGTH);
            }
        }
    }
}