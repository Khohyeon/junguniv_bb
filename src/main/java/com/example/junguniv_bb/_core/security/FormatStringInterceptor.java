package com.example.junguniv_bb._core.security;

import com.example.junguniv_bb._core.exception.Exception400;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.regex.Pattern;


/**
 * Format String 공격을 방지하기 위한 인터셉터입니다.
 * 요청 파라미터 값에서 포맷 스트링 공격에 사용될 수 있는 패턴을 검사합니다.
 */
@Component
public class FormatStringInterceptor implements HandlerInterceptor {
    
    private static final Logger log = LoggerFactory.getLogger(FormatStringInterceptor.class);
    
    /**
     * 포맷 스트링 공격 패턴을 정의합니다.
     * 기본 포맷 지정자, 확장 포맷 지정자, MessageFormat 스타일, 템플릿 문자열, 비정상적인 포맷 문자 등을 포함합니다.
     */
    private static final Pattern[] FORMAT_STRING_PATTERNS = {
        Pattern.compile(".*%(n|s|d|x|X|e|f|g|o|p|c|h|l|L|\\d+\\$).*"),  // 기본 포맷 지정자
        Pattern.compile(".*%[0-9]*[.]?[0-9]*[diuoxXfFeEgGaAcspn%].*"),  // 확장 포맷 지정자
        Pattern.compile(".*\\{[0-9]+\\}.*"),  // MessageFormat 스타일
        Pattern.compile(".*\\$\\{.*\\}.*"),   // 템플릿 문자열
        Pattern.compile(".*%[^a-zA-Z0-9].*")  // 비정상적인 포맷 문자
    };

    /**
     * HTTP 요청을 가로채서 포맷 스트링 공격 패턴을 검사합니다.
     *
     * @param request  HttpServletRequest 요청 객체
     * @param response HttpServletResponse 응답 객체
     * @param handler  요청을 처리할 핸들러 객체
     * @return 요청 처리 계속 여부 (true: 계속, false: 중단)
     * @throws Exception400 유효하지 않은 입력값이 감지될 경우 예외 발생
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 모든 요청 파라미터에 대해 검사
        request.getParameterMap().forEach((key, values) -> {
            for (String value : values) {
                if (value != null) {
                    // Format String 패턴 검사
                    for (Pattern pattern : FORMAT_STRING_PATTERNS) {
                        if (pattern.matcher(value).matches()) {
                            log.warn("Format String attack detected in parameter: {} with value: {}", key, value);
                            throw new Exception400("유효하지 않은 문자가 포함되어 있습니다: " + key);
                        }
                    }
                    
                    // 추가적인 위험 문자 검사
                    if (containsDangerousCharacters(value)) {
                        log.warn("Dangerous characters detected in parameter: {} with value: {}", key, value);
                        throw new Exception400("유효하지 않은 문자가 포함되어 있습니다: " + key);
                    }
                }
            }
        });

        return true;
    }

    /**
     * 포맷 문자열 관련 위험 문자를 검사합니다.
     *
     * @param value 검사할 문자열 값
     * @return 위험 문자 포함 여부 (true: 포함, false: 미포함)
     */
    private boolean containsDangerousCharacters(String value) {
        // 포맷 문자열 관련 위험 문자 검사
        return value.contains("%n") || 
               value.contains("%s") || 
               value.contains("%d") || 
               value.contains("%x") || 
               value.contains("%f") ||
               value.contains("\\u") ||  // 유니코드 이스케이프
               value.contains("\\x");    // 16진수 이스케이프
    }
}