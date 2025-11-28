package com.studiz.domain.calendar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@Schema(description = "달력 월별 요약 응답")
public class CalendarMonthResponse {

    @Schema(description = "연도", example = "2024")
    private final int year;

    @Schema(description = "월 (1-12)", example = "1")
    private final int month;

    @Schema(description = "해당 월의 각 날짜별 요약 정보")
    private final List<DaySummary> days;

    @Getter
    @Builder
    @Schema(description = "하루 요약 정보")
    public static class DaySummary {
        @Schema(description = "날짜", example = "2024-01-15")
        private final LocalDate date;

        @Schema(description = "완료율 (0-100). 완료된 TodoMember 수 / 전체 TodoMember 수 * 100", example = "75.5")
        private final double completionRate;

        @Schema(description = "D-day (마감일까지 남은 일수). 음수면 지난 날짜, 0이면 오늘, 양수면 남은 일수", example = "5")
        private final Long dDay;

        @Schema(description = "해당 날짜에 마감인 Todo 개수", example = "3")
        private final int todoCount;
    }
}

