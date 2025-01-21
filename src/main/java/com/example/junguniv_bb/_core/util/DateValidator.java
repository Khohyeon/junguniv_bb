package com.example.junguniv_bb._core.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateValidator {

    // 날짜 형식 검증 (yyyy-MM-dd)
    public static boolean isValidDate(String date) {
        if (date == null || date.isEmpty()) {
            return false;
        }
        try {
            LocalDate.parse(date); // 날짜 파싱
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // 날짜 범위 검증 (startDate <= endDate)
    public static boolean isValidDateRange(String startDate, String endDate) {
        if (!isValidDate(startDate) || !isValidDate(endDate)) {
            return false; // 날짜 형식이 유효하지 않으면 false
        }
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return !start.isAfter(end); // startDate가 endDate보다 이후면 false
    }
}