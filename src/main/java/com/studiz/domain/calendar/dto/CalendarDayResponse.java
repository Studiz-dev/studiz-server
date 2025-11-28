package com.studiz.domain.calendar.dto;

import com.studiz.domain.todo.dto.TodoResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CalendarDayResponse {

    private final LocalDate date;
    private final List<TodoResponse> todos;
    private final double overallCompletionRate;
    private final Long dDay;
}

