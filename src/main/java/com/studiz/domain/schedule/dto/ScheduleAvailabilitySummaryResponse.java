package com.studiz.domain.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "가능 시간 통계 응답 (Heatmap용)")
public class ScheduleAvailabilitySummaryResponse {

    @Schema(description = "슬롯별 가능 인원 수 목록")
    private final List<SlotAvailability> slots;

    @Getter
    @Builder
    @Schema(description = "슬롯별 가능 인원 수")
    public static class SlotAvailability {
        @Schema(description = "시간 슬롯 ID", example = "123e4567-e89b-12d3-a456-426614174002")
        private final UUID slotId;

        @Schema(description = "해당 슬롯에 가능하다고 표시한 인원 수", example = "5")
        private final int availableCount;
    }
}

