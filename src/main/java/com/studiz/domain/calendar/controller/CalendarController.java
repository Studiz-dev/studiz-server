package com.studiz.domain.calendar.controller;

import com.studiz.domain.calendar.dto.CalendarDayResponse;
import com.studiz.domain.calendar.dto.CalendarMonthResponse;
import com.studiz.domain.calendar.service.CalendarService;
import com.studiz.domain.user.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Calendar", description = "달력 및 통계 API")
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping
    @Operation(
            summary = "달력 월별 요약 조회",
            description = "특정 월의 모든 날짜에 대한 요약 정보를 조회합니다. 각 날짜별로 완료율, D-day, Todo 개수가 포함됩니다. 완료율은 (완료된 TodoMember 수 / 전체 TodoMember 수) * 100으로 계산됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "403", description = "스터디 멤버만 접근 가능"),
            @ApiResponse(responseCode = "404", description = "스터디를 찾을 수 없음")
    })
    public ResponseEntity<CalendarMonthResponse> getMonthSummary(
            @Parameter(description = "스터디 ID", required = true)
            @RequestParam UUID studyId,
            @Parameter(description = "연도", required = true, example = "2024")
            @RequestParam int year,
            @Parameter(description = "월 (1-12)", required = true, example = "1")
            @RequestParam int month,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(calendarService.getMonthSummary(studyId, year, month, userPrincipal.getUser()));
    }

    @GetMapping("/{date}")
    @Operation(
            summary = "달력 일별 상세 조회",
            description = "특정 날짜의 상세 정보를 조회합니다. 해당 날짜에 마감인 Todo 목록, 전체 완료율, D-day가 포함됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "403", description = "스터디 멤버만 접근 가능"),
            @ApiResponse(responseCode = "404", description = "스터디를 찾을 수 없음")
    })
    public ResponseEntity<CalendarDayResponse> getDayDetail(
            @Parameter(description = "스터디 ID", required = true)
            @RequestParam UUID studyId,
            @Parameter(description = "조회할 날짜 (YYYY-MM-DD 형식)", required = true, example = "2024-01-15")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(calendarService.getDayDetail(studyId, date, userPrincipal.getUser()));
    }
}

