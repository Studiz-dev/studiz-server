package com.studiz.domain.calendar.controller;

import com.studiz.domain.calendar.dto.CalendarDayResponse;
import com.studiz.domain.calendar.dto.CalendarMonthResponse;
import com.studiz.domain.calendar.service.CalendarService;
import com.studiz.domain.user.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping
    public ResponseEntity<CalendarMonthResponse> getMonthSummary(
            @RequestParam UUID studyId,
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(calendarService.getMonthSummary(studyId, year, month, userPrincipal.getUser()));
    }

    @GetMapping("/{date}")
    public ResponseEntity<CalendarDayResponse> getDayDetail(
            @RequestParam UUID studyId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(calendarService.getDayDetail(studyId, date, userPrincipal.getUser()));
    }
}

