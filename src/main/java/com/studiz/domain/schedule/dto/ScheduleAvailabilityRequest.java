package com.studiz.domain.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Schema(
        description = "가능 시간 등록 요청",
        example = "{\n" +
                "  \"availableSlotIds\": [\n" +
                "    \"123e4567-e89b-12d3-a456-426614174002\",\n" +
                "    \"123e4567-e89b-12d3-a456-426614174003\",\n" +
                "    \"123e4567-e89b-12d3-a456-426614174004\"\n" +
                "  ]\n" +
                "}"
)
public class ScheduleAvailabilityRequest {

    @NotEmpty(message = "가능한 시간 슬롯을 최소 하나 이상 선택해야 합니다.")
    @Schema(
            description = "가능한 시간 슬롯 ID 리스트. 이 리스트에 포함된 슬롯만 가능으로 표시되고, 나머지는 불가능으로 처리됩니다.",
            example = "[\"123e4567-e89b-12d3-a456-426614174002\", \"123e4567-e89b-12d3-a456-426614174003\"]",
            required = true
    )
    private List<UUID> availableSlotIds;
}

