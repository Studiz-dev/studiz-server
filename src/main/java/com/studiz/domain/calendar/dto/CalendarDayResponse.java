package com.studiz.domain.calendar.dto;

import com.studiz.domain.todo.dto.TodoResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@Schema(description = "달력 일별 상세 응답")
public class CalendarDayResponse {

    @Schema(description = "조회한 날짜", example = "2024-01-15")
    private final LocalDate date;

    @Schema(description = "해당 날짜에 마감인 Todo 목록")
    private final List<TodoResponse> todos;

    @Schema(description = "전체 완료율 (0-100). 해당 날짜의 모든 Todo의 완료율 평균", example = "80.5")
    private final double overallCompletionRate;

    @Schema(description = "D-day (마감일까지 남은 일수). 음수면 지난 날짜, 0이면 오늘, 양수면 남은 일수", example = "5")
    private final Long dDay;
}

