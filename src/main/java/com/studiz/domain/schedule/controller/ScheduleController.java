package com.studiz.domain.schedule.controller;

import com.studiz.domain.schedule.dto.ScheduleCreateRequest;
import com.studiz.domain.schedule.dto.ScheduleDetailResponse;
import com.studiz.domain.schedule.dto.ScheduleResponse;
import com.studiz.domain.schedule.service.ScheduleService;
import com.studiz.domain.user.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/studies/{studyId}/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedule", description = "스터디 일정 관리 API")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    @Operation(
            summary = "일정 생성",
            description = "스터디의 새로운 일정을 생성합니다.\n\n" +
                    "**권한**: 스터디장만 생성 가능\n\n" +
                    "**동작 방식**:\n" +
                    "- 시작일부터 종료일까지의 모든 날짜에 대해 1시간 단위 시간 슬롯이 자동 생성됩니다.\n" +
                    "- 예: 2024-01-15 ~ 2024-01-16이면, 각 날짜의 00:00부터 23:00까지 24개의 슬롯이 생성됩니다.\n" +
                    "- 생성된 슬롯은 일정 상세 조회 API로 확인할 수 있습니다.\n\n" +
                    "**요청 예시**:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"title\": \"스터디 모임 일정 조율\",\n" +
                    "  \"startDate\": \"2024-01-15\",\n" +
                    "  \"endDate\": \"2024-01-20\"\n" +
                    "}\n" +
                    "```\n\n" +
                    "**주의사항**:\n" +
                    "- 시작일은 종료일보다 이전이어야 합니다.\n" +
                    "- 날짜 범위가 너무 크면 많은 슬롯이 생성되므로 주의하세요."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 생성 성공", content = @Content(schema = @Schema(implementation = ScheduleResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 날짜 범위 (시작일이 종료일보다 늦음)"),
            @ApiResponse(responseCode = "403", description = "스터디장 권한 필요"),
            @ApiResponse(responseCode = "404", description = "스터디를 찾을 수 없음")
    })
    public ResponseEntity<ScheduleResponse> createSchedule(
            @Parameter(description = "스터디 ID", required = true)
            @PathVariable UUID studyId,
            @Valid @RequestBody ScheduleCreateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(scheduleService.createSchedule(studyId, request, userPrincipal.getUser()));
    }

    @GetMapping
    @Operation(
            summary = "스케줄 조회",
            description = "스터디의 일정 목록을 조회합니다.\n\n" +
                    "**파라미터**:\n" +
                    "- `month` (선택): 특정 월의 일정만 필터링. 형식: `YYYY-MM` (예: `2024-01`)\n" +
                    "- `month`를 생략하면 모든 일정을 조회합니다.\n\n" +
                    "**필터링 로직**:\n" +
                    "- 일정의 시작일 또는 종료일이 해당 월에 포함되면 조회됩니다.\n" +
                    "- 예: 일정이 2024-01-15 ~ 2024-01-20이면, `month=2024-01`로 조회 가능합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ScheduleResponse.class))),
            @ApiResponse(responseCode = "403", description = "스터디 멤버만 접근 가능"),
            @ApiResponse(responseCode = "404", description = "스터디를 찾을 수 없음")
    })
    public ResponseEntity<List<ScheduleResponse>> getSchedules(
            @Parameter(description = "스터디 ID", required = true)
            @PathVariable UUID studyId,
            @Parameter(description = "조회할 월 (YYYY-MM 형식, 선택사항)")
            @RequestParam(required = false) String month,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(scheduleService.getSchedules(studyId, month, userPrincipal.getUser()));
    }

    @GetMapping("/{scheduleId}")
    @Operation(
            summary = "스케줄 상세 조회",
            description = "특정 일정의 상세 정보를 조회합니다.\n\n" +
                    "**응답 내용**:\n" +
                    "- 일정 기본 정보 (제목, 시작일, 종료일)\n" +
                    "- 확정된 시간 슬롯 ID (확정되지 않았으면 null)\n" +
                    "- 모든 시간 슬롯 목록 (시작 시간 순으로 정렬)\n\n" +
                    "**시간 슬롯**:\n" +
                    "- 각 슬롯은 1시간 단위입니다.\n" +
                    "- `startTime`과 `endTime`으로 시간 범위를 확인할 수 있습니다.\n" +
                    "- 이 슬롯 ID를 사용하여 가능 시간을 등록할 수 있습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ScheduleDetailResponse.class))),
            @ApiResponse(responseCode = "403", description = "스터디 멤버만 접근 가능"),
            @ApiResponse(responseCode = "404", description = "일정을 찾을 수 없음")
    })
    public ResponseEntity<ScheduleDetailResponse> getScheduleDetail(
            @Parameter(description = "스터디 ID", required = true)
            @PathVariable UUID studyId,
            @Parameter(description = "일정 ID", required = true)
            @PathVariable UUID scheduleId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(scheduleService.getScheduleDetail(studyId, scheduleId, userPrincipal.getUser()));
    }
}

