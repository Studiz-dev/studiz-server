package com.studiz.domain.schedule.dto;

import com.studiz.domain.schedule.entity.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class ScheduleResponse {

    private final UUID id;
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
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

