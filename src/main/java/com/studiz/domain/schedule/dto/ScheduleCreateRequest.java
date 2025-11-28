package com.studiz.domain.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Schema(description = "일정 생성 요청")
public class ScheduleCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    @Schema(description = "일정 제목", example = "스터디 모임 일정 조율", required = true)
    private String title;

    @NotNull(message = "시작일은 필수입니다.")
    @Schema(description = "일정 시작일 (YYYY-MM-DD 형식)", example = "2024-01-15", required = true)
    private LocalDate startDate;

    @NotNull(message = "종료일은 필수입니다.")
    @Schema(description = "일정 종료일 (YYYY-MM-DD 형식). 시작일보다 이후여야 합니다.", example = "2024-01-20", required = true)
    private LocalDate endDate;
}

