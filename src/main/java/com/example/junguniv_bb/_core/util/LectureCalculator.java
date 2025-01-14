package com.example.junguniv_bb._core.util;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class LectureCalculator {

    private static final Map<String, DayOfWeek> KOREAN_TO_DAY_OF_WEEK = Map.of(
            "월요일", DayOfWeek.MONDAY,
            "화요일", DayOfWeek.TUESDAY,
            "수요일", DayOfWeek.WEDNESDAY,
            "목요일", DayOfWeek.THURSDAY,
            "금요일", DayOfWeek.FRIDAY,
            "토요일", DayOfWeek.SATURDAY,
            "일요일", DayOfWeek.SUNDAY
    );

    public static DayOfWeek getDayOfWeek(String koreanDay) {
        if (koreanDay == null || koreanDay.isEmpty()) {
            return null; // 빈 값 또는 null에 대해 명시적으로 처리
        }
        return KOREAN_TO_DAY_OF_WEEK.get(koreanDay);
    }

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static long calculateLectureDurationHours(String startTime, String endTime) {
        LocalTime start = LocalTime.parse(startTime, TIME_FORMATTER);
        LocalTime end = LocalTime.parse(endTime, TIME_FORMATTER);
        return Duration.between(start, end).toMinutes() / 60; // 시간 단위
    }

    public static List<LocalDate> calculateLectureDates(String startDateStr, String endDateStr, String lectureTime) {
        LocalDate startDate = LocalDate.parse(startDateStr, DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(endDateStr, DATE_FORMATTER);
        DayOfWeek lectureDay = getDayOfWeek(lectureTime);

        List<LocalDate> lectureDates = calculateLectureDates(startDate, endDate, lectureDay);

        // 날짜 기준으로 정렬 (이미 날짜 순서로 생성되지만, 명시적으로 정렬 보장)
        lectureDates.sort(Comparator.naturalOrder());

        return lectureDates;
    }

    private static List<LocalDate> calculateLectureDates(LocalDate startDate, LocalDate endDate, DayOfWeek lectureDay) {
        List<LocalDate> lectureDates = new ArrayList<>();
        if (lectureDay == null) {
            return lectureDates;
        }
        LocalDate current = startDate.with(TemporalAdjusters.nextOrSame(lectureDay)); // 시작일의 해당 요일부터 시작
        while (!current.isAfter(endDate)) {
            lectureDates.add(current);
            current = current.plus(1, ChronoUnit.WEEKS); // 1주일씩 증가
        }

        return lectureDates;
    }


    public static long dateCalculator(String startDateStr, String endDateStr, String lectureTime, String startTime, String endTime) {
        long durationHours = calculateLectureDurationHours(startTime, endTime);
        List<LocalDate> lectureDates = calculateLectureDates(startDateStr, endDateStr, lectureTime);
        logLectureDetails(startDateStr, endDateStr, lectureTime, lectureDates, durationHours);
        return lectureDates.size() * durationHours;
    }

    public static long dateCalculatorBySize(String startDateStr, String endDateStr, String lectureTime) {
        List<LocalDate> lectureDates = calculateLectureDates(startDateStr, endDateStr, lectureTime);
        logLectureDetails(startDateStr, endDateStr, lectureTime, lectureDates, 0); // 시간 출력 없이 로그
        return lectureDates.size();
    }

    public static Map<String, Long> calculateLectureCountByDay(String startDateStr, String endDateStr, List<String> lectureDays) {
        Map<String, Long> lectureCountByDay = new HashMap<>();
        LocalDate startDate = LocalDate.parse(startDateStr, DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(endDateStr, DATE_FORMATTER);

        for (String lectureDay : lectureDays) {
            DayOfWeek dayOfWeek = KOREAN_TO_DAY_OF_WEEK.get(lectureDay);
            if (dayOfWeek != null) {
                List<LocalDate> lectureDates = calculateLectureDates(startDate, endDate, dayOfWeek);
                lectureCountByDay.put(lectureDay, (long) lectureDates.size());
            } else {
                throw new IllegalArgumentException("Invalid day provided: " + lectureDay);
            }
        }

        return lectureCountByDay;
    }

    public static String calculateLectureCountByDayAsString(String startDateStr, String endDateStr, List<String> lectureDays) {
        Map<String, Long> lectureCountByDay = calculateLectureCountByDay(startDateStr, endDateStr, lectureDays);

        // Map 데이터를 문자열로 변환
        StringBuilder responseBuilder = new StringBuilder();
        lectureCountByDay.forEach((day, count) -> {
            responseBuilder.append(day).append(": ").append(count).append("\n");
        });

        return responseBuilder.toString().trim(); // 마지막 줄바꿈 제거
    }

    public static List<String> calculateLectureDatesWithDayAndTime(String startDateStr, String endDateStr, String lectureTime, String startTime, String endTime) {
        List<String> lectureDatesWithDetails = new ArrayList<>();

        // 강의 요일과 시간 변환
        DayOfWeek lectureDay = KOREAN_TO_DAY_OF_WEEK.get(lectureTime);
        if (lectureDay == null) {
            throw new IllegalArgumentException("Invalid lecture day provided: " + lectureTime);
        }

        LocalDate startDate = LocalDate.parse(startDateStr, DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(endDateStr, DATE_FORMATTER);

        // 강의 날짜 계산
        List<LocalDate> lectureDates = calculateLectureDates(startDate, endDate, lectureDay);

        // 날짜와 시간 포함된 문자열 생성
        for (LocalDate date : lectureDates) {
            String formattedDate = date.format(DATE_FORMATTER);
            lectureDatesWithDetails.add(formattedDate + " (" + lectureDay.getDisplayName(java.time.format.TextStyle.FULL, Locale.KOREAN) + ") " + startTime + " - " + endTime);
        }

        return lectureDatesWithDetails;
    }

    private static void logLectureDetails(String startDateStr, String endDateStr, String lectureTime, List<LocalDate> lectureDates, long durationHours) {
        DayOfWeek lectureDay = KOREAN_TO_DAY_OF_WEEK.get(lectureTime);
        System.out.println("강의 기간: " + startDateStr + " ~ " + endDateStr);
        System.out.println("강의 요일: " + lectureDay.getDisplayName(java.time.format.TextStyle.FULL, Locale.KOREAN));
        if (durationHours > 0) {
            System.out.println("강의 시간 (시간): " + durationHours);
        }
        System.out.println("총 강의 개수: " + lectureDates.size());
        System.out.println("강의 날짜:");
        lectureDates.forEach(date -> System.out.println(date.format(DATE_FORMATTER)));
    }
}