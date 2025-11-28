package com.studiz.domain.schedule.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class ScheduleAvailabilityRequest {

    @NotEmpty(message = "가능한 시간 슬롯을 최소 하나 이상 선택해야 합니다.")
    private List<UUID> availableSlotIds;
}

