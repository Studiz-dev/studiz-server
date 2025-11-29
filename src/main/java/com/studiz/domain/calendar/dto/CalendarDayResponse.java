package com.studiz.domain.calendar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "달력 일별 상세 응답")
public class CalendarDayResponse {

    @Schema(description = "조회한 날짜", example = "2024-01-15")
    private final LocalDate date;

    @Schema(description = "해당 날짜에 확정된 일정 목록 (시간 순으로 정렬됨, 프론트에서 최대 3개만 표시)", 
            example = "[]")
    private final List<ScheduleInfo> schedules;

    @Getter
    @Builder
    @Schema(description = "일정 정보")
    public static class ScheduleInfo {
        @Schema(description = "일정 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        private final UUID id;

        @Schema(description = "일정 제목", example = "스터디 모임")
        private final String title;

        @Schema(description = "장소", example = "서울시 강남구", nullable = true)
        private final String location;

        @Schema(description = "일정 시간 (YYYY M DD HH:mm 형식)", example = "2024 1 15 14:00")
        private final String scheduleTime;

        @Schema(description = "D-Day (오늘 기준 남은 일수)", example = "5")
        private final Long dDay;
    }
}

