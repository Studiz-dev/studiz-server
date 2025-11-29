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
                    "- 지정한 날짜에 대해 1시간 단위 시간 슬롯이 자동 생성됩니다.\n" +
                    "- 예: 2024-01-15이면, 해당 날짜의 00:00부터 23:00까지 24개의 슬롯이 생성됩니다.\n" +
                    "- 생성된 슬롯은 일정 상세 조회 API로 확인할 수 있습니다.\n" +
                    "- 팀장이 시간을 확정하면 일정이 완료됩니다.\n\n" +
                    "**요청 예시**:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"title\": \"스터디 모임 일정 조율\",\n" +
                    "  \"startDate\": \"2024-01-15\"\n" +
                    "}\n" +
                    "```"
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
            summary = "일정 목록 조회",
            description = "스터디의 일정 목록을 조회합니다.\n\n" +
                    "**응답 내용**:\n" +
                    "- 일정 이름 (title)\n" +
                    "- 장소 (location)\n" +
                    "- 일정 시간 (YYYY M DD HH:mm 형식, 확정된 일정만)\n" +
                    "- D-Day (오늘 기준 남은 일수, 확정된 일정만)\n\n" +
                    "**정렬**: 확정된 일정 우선, 그 다음 날짜 오름차순"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ScheduleResponse.class))),
            @ApiResponse(responseCode = "403", description = "스터디 멤버만 접근 가능"),
            @ApiResponse(responseCode = "404", description = "스터디를 찾을 수 없음")
    })
    public ResponseEntity<List<ScheduleResponse>> getSchedules(
            @Parameter(description = "스터디 ID", required = true)
            @PathVariable UUID studyId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(scheduleService.getSchedules(studyId, null, userPrincipal.getUser()));
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

