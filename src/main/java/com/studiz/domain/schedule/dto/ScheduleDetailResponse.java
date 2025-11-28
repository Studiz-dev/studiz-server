package com.studiz.domain.schedule.dto;

import com.studiz.domain.schedule.entity.Schedule;
import com.studiz.domain.schedule.entity.ScheduleSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "일정 상세 정보 (시간 슬롯 포함)")
public class ScheduleDetailResponse {

    @Schema(description = "일정 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private final UUID id;

    @Schema(description = "일정 제목", example = "스터디 모임 일정 조율")
    private final String title;

    @Schema(description = "일정 시작일", example = "2024-01-15")
    private final java.time.LocalDate startDate;

    @Schema(description = "일정 종료일", example = "2024-01-20")
    private final java.time.LocalDate endDate;

    @Schema(description = "확정된 시간 슬롯 ID (확정되지 않았으면 null)", example = "123e4567-e89b-12d3-a456-426614174001", nullable = true)
    private final UUID confirmedSlotId;

    @Schema(description = "30분 단위 시간 슬롯 목록 (시작 시간 순으로 정렬됨)")
    private final List<ScheduleSlotResponse> slots;

    public static ScheduleDetailResponse from(Schedule schedule, List<ScheduleSlot> slots) {
        return ScheduleDetailResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .confirmedSlotId(schedule.getConfirmedSlot() != null ? schedule.getConfirmedSlot().getId() : null)
                .slots(slots.stream()
                        .map(ScheduleSlotResponse::from)
                        .toList())
                .build();
    }

    @Getter
    @Builder
    @Schema(description = "시간 슬롯 정보 (30분 단위)")
    public static class ScheduleSlotResponse {
        @Schema(description = "슬롯 ID", example = "123e4567-e89b-12d3-a456-426614174002")
        private final UUID id;

        @Schema(description = "슬롯 시작 시간", example = "2024-01-15T09:00:00")
        private final LocalDateTime startTime;

        @Schema(description = "슬롯 종료 시간 (시작 시간 + 30분)", example = "2024-01-15T09:30:00")
        private final LocalDateTime endTime;

        public static ScheduleSlotResponse from(ScheduleSlot slot) {
            return ScheduleSlotResponse.builder()
                    .id(slot.getId())
                    .startTime(slot.getStartTime())
                    .endTime(slot.getEndTime())
                    .build();
        }
    }
}

