package com.studiz.domain.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
@Schema(description = "일정 확정 요청")
public class ScheduleConfirmRequest {

    @NotNull(message = "확정할 슬롯 ID는 필수입니다.")
    @Schema(description = "확정할 시간 슬롯 ID", example = "123e4567-e89b-12d3-a456-426614174002", required = true)
    private UUID slotId;
}

