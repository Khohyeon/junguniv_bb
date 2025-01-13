package com.example.junguniv_bb._core.util;

import java.time.LocalDateTime;

public class DateTimeUtil {

    // String을 LocalDateTime으로 변환
    public static LocalDateTime parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateStr + "T00:00:00"); // 날짜만 있을 경우 시간 기본값 추가
    }

    public static LocalDateTime parseEndDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateStr + "T23:59:59");
    }
}