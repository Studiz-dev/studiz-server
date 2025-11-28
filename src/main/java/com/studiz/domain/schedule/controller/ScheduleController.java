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
            description = "스터디의 새로운 일정을 생성합니다. 스터디장만 생성할 수 있으며, 시작일-종료일 범위에서 30분 단위 시간 슬롯이 자동으로 생성됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 생성 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 날짜 범위"),
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
            description = "스터디의 일정 목록을 조회합니다. month 파라미터로 특정 월의 일정만 필터링할 수 있습니다. (형식: YYYY-MM)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
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
            description = "특정 일정의 상세 정보를 조회합니다. 시간 슬롯 목록이 포함됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
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

