package com.studiz.domain.schedule.dto;

import com.studiz.domain.schedule.entity.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "일정 정보")
public class ScheduleResponse {

    @Schema(description = "일정 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private final UUID id;

    @Schema(description = "일정 제목", example = "스터디 모임 일정 조율")
    private final String title;

    @Schema(description = "장소", example = "서울시 강남구", nullable = true)
    private final String location;

    @Schema(description = "일정 시간 (YYYY M DD HH:mm 형식, 확정되지 않았으면 null)", example = "2024 1 15 14:00", nullable = true)
    private final String scheduleTime;

    @Schema(description = "D-Day (오늘 기준 남은 일수, 확정되지 않았으면 null)", example = "5", nullable = true)
    private final Long dDay;

    public static ScheduleResponse from(Schedule schedule) {
        String scheduleTime = null;
        Long dDay = null;

        if (schedule.getConfirmedSlot() != null) {
            LocalDateTime confirmedTime = schedule.getConfirmedSlot().getStartTime();
            scheduleTime = confirmedTime.format(DateTimeFormatter.ofPattern("yyyy M d HH:mm"));
            
            LocalDate today = LocalDate.now();
            LocalDate scheduleDate = confirmedTime.toLocalDate();
            dDay = ChronoUnit.DAYS.between(today, scheduleDate);
        }

        return ScheduleResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .location(schedule.getLocation())
                .scheduleTime(scheduleTime)
                .dDay(dDay)
                .build();
    }
}

