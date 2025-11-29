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

        @Schema(description = "해당 날짜에 확정된 일정 개수", example = "2")
        private final int scheduleCount;
    }
}

