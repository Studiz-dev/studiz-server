package com.studiz.domain.calendar.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CalendarMonthResponse {

    private final int year;
    private final int month;
    private final List<DaySummary> days;

    @Getter
    @Builder
    public static class DaySummary {
        private final LocalDate date;
        private final double completionRate;
        private final Long dDay;
        private final int todoCount;
    }
}

