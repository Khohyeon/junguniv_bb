package com.example.junguniv_bb._core.util;

import com.example.junguniv_bb._core.exception.Exception400;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RoomTimeParser {

    public static class RoomTime {

        // 요일 문자열 추출 메서드
        public static List<String> extractDays(String roomTime) {
            if (roomTime == null || roomTime.isEmpty()) {
                return new ArrayList<>();
            }
            String[] entries = roomTime.split(","); // 쉼표로 구분된 각 항목 분리
            List<String> days = new ArrayList<>();
            for (String entry : entries) {
                try {
                    String[] parts = entry.trim().split(" ");
                    if (parts.length < 2) {
                        continue; // 강의 요일이 제대로 형성되지 않은 경우 건너뛰기
                    }
                    String day = parts[0]; // 요일 부분만 추출
                    days.add(day);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Parsing error: " + entry);
                }
            }
            return days;
        }

        // 시간대 문자열 추출 메서드
        public static List<String> extractTimeRanges(String roomTime) {
            if (roomTime == null || roomTime.isEmpty()) {
                return new ArrayList<>();
            }

            String[] entries = roomTime.split(","); // 쉼표로 구분된 각 항목 분리
            List<String> timeRanges = new ArrayList<>();

            for (String entry : entries) {
                try {
                    String[] parts = entry.trim().split(" ");
                    if (parts.length < 2) {
                        continue; // 강의 시간이 제대로 형성되지 않은 경우 건너뛰기
                    }
                    String timeRange = parts[1]; // 시간 부분만 추출
                    timeRanges.add(timeRange);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Parsing error: " + entry);
                }
            }

            return timeRanges;
        }

        // 요일과 시간대의 개수를 검증하는 메서드
        public static void validateDaysAndTimes(String roomTime) {
            List<String> days = extractDays(roomTime);
            List<String> times = extractTimeRanges(roomTime);

            // days와 times의 길이가 일치하지 않으면 예외 던지기
            if (days.size() != times.size()) {
                throw new Exception400("강의 스케줄 요일 개수와 시간 개수가 일치하지 않습니다.");
            }
        }
    }
}
