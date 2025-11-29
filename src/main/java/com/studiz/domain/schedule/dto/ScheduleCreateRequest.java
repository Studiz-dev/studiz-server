package com.studiz.domain.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Schema(
        description = "일정 생성 요청",
        example = "{\n" +
                "  \"title\": \"스터디 모임 일정 조율\",\n" +
                "  \"startDate\": \"2024-01-15\",\n" +
                "  \"location\": \"서울시 강남구\"\n" +
                "}"
)
public class ScheduleCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    @Schema(description = "일정 제목", example = "스터디 모임 일정 조율", required = true)
    private String title;

    @NotNull(message = "시작일은 필수입니다.")
    @Schema(description = "일정 날짜 (YYYY-MM-DD 형식)", example = "2024-01-15", required = true)
    private LocalDate startDate;

    @Schema(description = "장소 (선택사항)", example = "서울시 강남구", required = false)
    private String location;
}

