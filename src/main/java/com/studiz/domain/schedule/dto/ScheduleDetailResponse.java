package com.studiz.domain.schedule.dto;

import com.studiz.domain.schedule.entity.Schedule;
import com.studiz.domain.schedule.entity.ScheduleSlot;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ScheduleDetailResponse {

    private final UUID id;
    private final String title;
    private final java.time.LocalDate startDate;
    private final java.time.LocalDate endDate;
    private final UUID confirmedSlotId;
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
    public static class ScheduleSlotResponse {
        private final UUID id;
        private final LocalDateTime startTime;
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

