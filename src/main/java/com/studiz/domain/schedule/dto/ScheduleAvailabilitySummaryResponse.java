package com.studiz.domain.schedule.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ScheduleAvailabilitySummaryResponse {

    private final List<SlotAvailability> slots;

    @Getter
    @Builder
    public static class SlotAvailability {
        private final UUID slotId;
        private final int availableCount;
    }
}

