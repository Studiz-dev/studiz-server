package com.studiz.domain.schedule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ScheduleConfirmRequest {

    @NotNull(message = "확정할 슬롯 ID는 필수입니다.")
    private UUID slotId;
}

