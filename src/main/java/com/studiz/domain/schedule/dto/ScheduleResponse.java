package com.studiz.domain.schedule.dto;

import com.studiz.domain.schedule.entity.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "일정 정보")
public class ScheduleResponse {

    @Schema(description = "일정 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private final UUID id;

    @Schema(description = "일정 제목", example = "스터디 모임 일정 조율")
    private final String title;

    @Schema(description = "일정 시작일", example = "2024-01-15")
    private final LocalDate startDate;

    @Schema(description = "일정 종료일", example = "2024-01-20")
    private final LocalDate endDate;

    @Schema(description = "확정된 시간 슬롯 ID (확정되지 않았으면 null)", example = "123e4567-e89b-12d3-a456-426614174001", nullable = true)
    private final UUID confirmedSlotId;

    public static ScheduleResponse from(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .confirmedSlotId(schedule.getConfirmedSlot() != null ? schedule.getConfirmedSlot().getId() : null)
                .build();
    }
}

