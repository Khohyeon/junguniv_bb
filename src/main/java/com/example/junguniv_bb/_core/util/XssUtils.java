package com.example.junguniv_bb._core.util;

import org.springframework.web.util.HtmlUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * XSS (Cross-Site Scripting)와 Path Traversal 공격 방지를 위한 유틸리티 클래스.
 * 입력 값에서 잠재적인 XSS 공격 스크립트를 제거하고 HTML 엔티티를 이스케이프하여 보안을 강화합니다.
 */
public class XssUtils {
    
    private static final Pattern[] XSS_PATTERNS = new Pattern[]{
        Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };

    private static final Pattern PATH_TRAVERSAL_PATTERN = Pattern.compile(".*[.][.].*");
    private static final Pattern UNSAFE_PATH_CHARS = Pattern.compile("[<>:\"|?*\\\\]");

    public static String stripXss(String value) {
        if (value == null) {
            return null;
        }

        // Path Traversal 검사 추가
        if (containsPathTraversal(value)) {
            return sanitizePath(value);
        }

        // HTML 이스케이프 처리
        value = HtmlUtils.htmlEscape(value);

        // XSS 패턴 제거
        for (Pattern pattern : XSS_PATTERNS) {
            value = pattern.matcher(value).replaceAll("");
        }

        return value;
    }

    /**
     * Path Traversal 공격 시도가 있는지 검사
     */
    public static boolean containsPathTraversal(String path) {
        if (path == null) {
            return false;
        }
        return PATH_TRAVERSAL_PATTERN.matcher(path).matches() || 
               UNSAFE_PATH_CHARS.matcher(path).find();
    }

    /**
     * 안전한 경로로 정규화
     */
    public static String sanitizePath(String path) {
        if (path == null) {
            return null;
        }

        // 상대 경로 요소 제거
        path = path.replaceAll("\\.\\.", "");
        
        // 위험한 문자 제거
        path = UNSAFE_PATH_CHARS.matcher(path).replaceAll("");

        try {
            // 경로 정규화
            Path normalizedPath = Paths.get(path).normalize();
            return normalizedPath.toString();
        } catch (Exception e) {
            // 경로 처리 중 오류 발생 시 빈 문자열 반환
            return "";
        }
    }

    public static boolean containsXss(String value) {
        if (value == null) {
            return false;
        }

        for (Pattern pattern : XSS_PATTERNS) {
            if (pattern.matcher(value).find()) {
                return true;
            }
        }

        return false;
    }
} 