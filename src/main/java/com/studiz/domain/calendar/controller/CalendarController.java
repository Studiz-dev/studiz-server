package com.studiz.domain.calendar.controller;

import com.studiz.domain.calendar.dto.CalendarDayResponse;
import com.studiz.domain.calendar.dto.CalendarMonthResponse;
import com.studiz.domain.calendar.service.CalendarService;
import com.studiz.domain.user.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
            description = "특정 월의 모든 날짜에 대한 요약 정보를 조회합니다.\n\n" +
                    "**요청 예시**:\n" +
                    "`GET /api/calendar?studyId={studyId}&year=2024&month=1`\n\n" +
                    "**응답 내용**:\n" +
                    "- `year`, `month`: 조회한 연도와 월\n" +
                    "- `days`: 해당 월의 각 날짜별 요약 정보 (배열)\n\n" +
                    "**각 날짜별 정보**:\n" +
                    "- `date`: 날짜 (YYYY-MM-DD)\n" +
                    "- `completionRate`: 완료율 (0-100)\n" +
                    "  - 계산식: (완료된 TodoMember 수 / 전체 TodoMember 수) * 100\n" +
                    "  - 해당 날짜에 마감인 모든 Todo의 TodoMember를 합산하여 계산\n" +
                    "- `dDay`: D-day (마감일까지 남은 일수)\n" +
                    "  - 음수: 지난 날짜\n" +
                    "  - 0: 오늘\n" +
                    "  - 양수: 남은 일수\n" +
                    "- `todoCount`: 해당 날짜에 마감인 Todo 개수\n\n" +
                    "**응답 예시**:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"year\": 2024,\n" +
                    "  \"month\": 1,\n" +
                    "  \"days\": [\n" +
                    "    {\n" +
                    "      \"date\": \"2024-01-15\",\n" +
                    "      \"completionRate\": 75.5,\n" +
                    "      \"dDay\": 5,\n" +
                    "      \"todoCount\": 3\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}\n" +
                    "```\n\n" +
                    "**사용 예시**:\n" +
                    "- 달력 UI에서 각 날짜의 색상 표시에 사용\n" +
                    "- 완료율이 높을수록 진한 색상, 낮을수록 연한 색상으로 표시\n" +
                    "- `dDay`를 사용하여 D-day 배지 표시"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = CalendarMonthResponse.class))),
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
            description = "특정 날짜의 상세 정보를 조회합니다.\n\n" +
                    "**요청 예시**:\n" +
                    "`GET /api/calendar/2024-01-15?studyId={studyId}`\n\n" +
                    "**응답 내용**:\n" +
                    "- `date`: 조회한 날짜 (YYYY-MM-DD)\n" +
                    "- `todos`: 해당 날짜에 마감인 Todo 목록 (배열)\n" +
                    "  - 각 Todo의 상세 정보 (제목, 설명, 완료율 등) 포함\n" +
                    "- `overallCompletionRate`: 전체 완료율 (0-100)\n" +
                    "  - 해당 날짜의 모든 Todo의 완료율을 평균낸 값\n" +
                    "  - 계산식: 각 Todo의 (완료된 TodoMember 수 / 전체 TodoMember 수)의 평균 * 100\n" +
                    "- `dDay`: D-day (마감일까지 남은 일수)\n" +
                    "  - 음수: 지난 날짜\n" +
                    "  - 0: 오늘\n" +
                    "  - 양수: 남은 일수\n\n" +
                    "**사용 예시**:\n" +
                    "- 달력에서 날짜를 클릭했을 때 상세 정보 표시\n" +
                    "- 해당 날짜의 Todo 목록과 진행 상황 확인\n" +
                    "- `overallCompletionRate`로 해당 날짜의 전체 진행률 표시"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = CalendarDayResponse.class))),
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

