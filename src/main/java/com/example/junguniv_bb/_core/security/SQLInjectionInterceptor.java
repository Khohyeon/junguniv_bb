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
 * SQL 인젝션 공격을 방지하기 위한 인터셉터입니다.
 * 요청 파라미터 값에서 SQL 인젝션 공격에 사용될 수 있는 패턴을 검사합니다.
 */
@Component
public class SQLInjectionInterceptor implements HandlerInterceptor {
    
    private static final Logger log = LoggerFactory.getLogger(SQLInjectionInterceptor.class);

    /**
     * SQL 인젝션 공격을 탐지하기 위한 정규 표현식 패턴 배열입니다.
     * 각 패턴은 SQL 인젝션 시도에 사용될 수 있는 특정 문자열 또는 구문을 나타냅니다.
     */
    private static final Pattern[] SQL_INJECTION_PATTERNS = {
        // 기본 SQL 인젝션 패턴
        Pattern.compile(".*[';].*"),  // 따옴표와 세미콜론
        Pattern.compile(".*(--|#|/\\*|\\*/).*"),  // SQL 주석
        Pattern.compile(".*(%3B|%2D%2D|%23).*"),  // URL 인코딩된 버전
        Pattern.compile(".*(0x[0-9a-fA-F]).*"),  // 16진수

        // 따옴표 기반 공격 패턴 강화
        Pattern.compile(".*\"\\s*(OR|AND)\\s*\".*"),  // "OR", "AND" 패턴
        Pattern.compile(".*\"\\s*[=<>]\\s*\".*"),     // " = ", " < ", " > " 패턴
        Pattern.compile(".*\"\\s*\\d+\\s*\".*"),      // 숫자를 포함한 따옴표 패턴
        Pattern.compile(".*\\s+OR\\s+[\"'].*"),       // OR 연산자와 따옴표
        Pattern.compile(".*\\s+AND\\s+[\"'].*"),      // AND 연산자와 따옴표
        Pattern.compile(".*[\"']\\s*=\\s*[\"'].*"),   // 따옴표 사이의 = 연산자
        Pattern.compile(".*[\"']\\s*(OR|AND)\\s+\\d+\\s*=\\s*\\d+.*"),  // 숫자 비교
        Pattern.compile(".*[\"']\\s*(OR|AND)\\s+[\"']\\d+[\"']\\s*=\\s*[\"']\\d+[\"'].*"),  // 따옴표로 둘러싸인 숫자 비교
        Pattern.compile(".*[\"']\\s*IS\\s+(NOT\\s+)?NULL.*"),  // NULL 비교
        Pattern.compile(".*[\"'].*?(=|<|>|<=|>=|<>|!=|IS|LIKE).*?[\"'].*"),  // 모든 비교 연산자

        // URL 인코딩된 공격 패턴
        Pattern.compile(".*(%22|%27).*(%20)?(OR|AND)(%20)?(%22|%27).*"),  // URL 인코딩된 따옴표와 연산자
        Pattern.compile(".*(%22|%27)(%20)?[=](%20)?(%22|%27).*"),  // URL 인코딩된 = 패턴

        // 기존 패턴들 유지
        Pattern.compile("(?i).*(INFORMATION_SCHEMA|PERFORMANCE_SCHEMA).*"),
        Pattern.compile("(?i).*(LOAD_FILE|INTO\\s+OUTFILE|INTO\\s+DUMPFILE).*"),
        Pattern.compile("(?i).*(PROCEDURE\\s+ANALYSE).*"),
        Pattern.compile("(?i).*(BENCHMARK|WAIT\\s+FOR\\s+DELAY).*"),
        Pattern.compile("(?i).*(SELECT\\s+IF|SELECT\\s+CASE).*"),
        Pattern.compile("(?i).*(@@version|@@datadir|@@basedir).*"),
        Pattern.compile("(?i).*(GRANT|REVOKE).*"),
        Pattern.compile("(?i).*(HANDLER|BINLOG).*"),
        Pattern.compile("(?i).*(KILL|FOUND_ROWS|ROW_COUNT).*"),
        Pattern.compile("(?i).*(CHARSET|COLLATE).*\\(.*\\)"),

        // SQLite 특화 공격 패턴
        Pattern.compile("(?i).*(ATTACH|DETACH)\\s+DATABASE.*"),
        Pattern.compile("(?i).*(PRAGMA).*"),
        Pattern.compile("(?i).*(VACUUM).*"),
        Pattern.compile("(?i).*(SQLITE_MASTER).*"),

        // UNION 기반 공격
        Pattern.compile("(?i).*\\s+UNION\\s+.*"),
        Pattern.compile("(?i).*\\s+JOIN\\s+.*"),

        // 시스템 명령어 실행 시도
        Pattern.compile("(?i).*(LOAD_EXTENSION|SQLITE_EXTENSION).*"),
        Pattern.compile("(?i).*(SYSTEM_USER|CURRENT_USER|SESSION_USER).*"),

        // 불법적인 문자 조합
        Pattern.compile(".*\\|\\|.*"),
        Pattern.compile("(?i).*(CAST|CONV|CHAR|HEX|UNICODE|BINARY).*"),
        Pattern.compile("(?i).*(MD5|SHA1|SHA2|ENCRYPT|DECODE).*"),

        // 시간 기반 공격 패턴 강화
        Pattern.compile("(?i).*(RANDOMBLOB|ZEROBLOB|SLEEP|BENCHMARK|WAIT\\s+FOR\\s+DELAY|PG_SLEEP|DBMS_PIPE\\.RECEIVE_MESSAGE).*"),
        Pattern.compile("(?i).*\\s+case\\s+.*\\s+when\\s+.*\\s+then\\s+.*\\s+else\\s+.*\\s+end\\s*.*"),
        Pattern.compile("(?i).*\\s+case\\s+.*\\s+when\\s+not\\s+null\\s+.*"),
        Pattern.compile("(?i).*\\s+case\\s+.*?\\(.*?\\).*?when\\s+.*"),
        Pattern.compile("(?i).*(DECODE|DELAY|LAG|LEAD)\\s*\\(.*\\).*"),
        Pattern.compile("(?i).*(GENERATE_SERIES|REGEXP_MATCHES|SIMILAR_TO).*"),

        // 기타 위험한 패턴
        Pattern.compile("(?i).*(GLOB|MATCH|REGEXP|RLIKE|SOUNDS\\s+LIKE).*"),
        Pattern.compile("(?i).*(REPLACE|SUBSTR|TRIM|INSERT|MID|UPDATE).*\\(.*\\)"),
        Pattern.compile("(?i).*(LAST_INSERT_ROWID|CHANGES|TOTAL_CHANGES|FOUND_ROWS).*"),
        Pattern.compile("(?i).*(TRUE|FALSE|1=1|1=2).*"),
        Pattern.compile("(?i).*(DROP|TRUNCATE|ALTER|RENAME).*"),
        Pattern.compile("(?i).*(EXEC|EXECUTE|XTYPE).*")
    };

    /**
     * HTTP 요청을 가로채서 SQL 인젝션 공격을 방지하기 위해 요청 파라미터 및 URL 경로를 검증합니다.
     *
     * @param request  HttpServletRequest 요청 객체
     * @param response HttpServletResponse 응답 객체
     * @param handler  요청을 처리할 핸들러 객체
     * @return 요청 처리 계속 여부 (true: 계속, false: 중단)
     * @throws Exception400 유효하지 않은 입력값이 감지될 경우 예외 발생
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            // URL 디코딩 검사 추가
            validateDecodedParameters(request);

            // 기존 검증 로직
            request.getParameterMap().forEach((key, values) -> {
                for (String value : values) {
                    if (value != null) {
                        // URL 디코딩된 값도 검사
                        String decodedValue = urlDecode(value);
                        checkSQLInjection(key, decodedValue);
                        
                        // 원래 값도 검사
                        checkSQLInjection(key, value);
                    }
                }
            });

            return true;
        } catch (Exception e) {
            log.error("Error in SQL injection validation", e);
            throw new Exception400("요청 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * URL 디코딩된 파라미터 값을 검증합니다.
     */
    private void validateDecodedParameters(HttpServletRequest request) {
        request.getParameterMap().forEach((key, values) -> {
            for (String value : values) {
                if (value != null) {
                    String decodedValue = urlDecode(value);
                    // 디코딩된 값에 대해 SQL 인젝션 패턴 검사
                    checkSQLInjection(key, decodedValue);
                }
            }
        });
    }

    /**
     * URL 디코딩을 수행합니다.
     */
    private String urlDecode(String value) {
        try {
            return java.net.URLDecoder.decode(value, "UTF-8");
        } catch (Exception e) {
            log.warn("Failed to decode URL parameter: {}", value);
            return value;
        }
    }

    /**
     * SQL 인젝션 패턴을 검사합니다.
     */
    private void checkSQLInjection(String key, String value) {
        for (Pattern pattern : SQL_INJECTION_PATTERNS) {
            if (pattern.matcher(value).matches()) {
                log.warn("SQL injection attempt detected in parameter: {} with value: {}", key, value);
                throw new Exception400("유효하지 않은 입력값이 포함되어 있습니다: " + key);
            }
        }
    }
}