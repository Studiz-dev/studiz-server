package com.studiz.domain.schedule.controller;

import com.studiz.domain.schedule.dto.ScheduleAvailabilityRequest;
import com.studiz.domain.schedule.dto.ScheduleAvailabilitySummaryResponse;
import com.studiz.domain.schedule.dto.ScheduleConfirmRequest;
import com.studiz.domain.schedule.service.ScheduleAvailabilityService;
import com.studiz.domain.schedule.service.ScheduleService;
import com.studiz.domain.user.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/schedules/{scheduleId}")
@RequiredArgsConstructor
@Tag(name = "Schedule Availability", description = "스케줄 가능 시간 관리 API")
public class ScheduleAvailabilityController {

    private final ScheduleAvailabilityService availabilityService;
    private final ScheduleService scheduleService;

    @PostMapping("/availability")
    @Operation(
            summary = "가능 시간 등록",
            description = "사용자가 가능한 시간 슬롯을 등록합니다. 기존에 등록한 시간이 있으면 업데이트됩니다. 가능한 슬롯 ID 리스트를 전송하면 해당 슬롯들만 가능으로 표시되고, 나머지는 불가능으로 처리됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "가능 시간 등록 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청"),
            @ApiResponse(responseCode = "403", description = "스터디 멤버만 접근 가능"),
            @ApiResponse(responseCode = "404", description = "일정을 찾을 수 없음")
    })
    public ResponseEntity<String> updateAvailability(
            @Parameter(description = "일정 ID", required = true)
            @PathVariable UUID scheduleId,
            @Parameter(description = "스터디 ID", required = true)
            @RequestParam UUID studyId,
            @Valid @RequestBody ScheduleAvailabilityRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        availabilityService.updateAvailability(studyId, scheduleId, request, userPrincipal.getUser());
        return ResponseEntity.ok("가능 시간이 등록되었습니다.");
    }

    @GetMapping("/availability/summary")
    @Operation(
            summary = "가능 시간 통계 조회 (Heatmap)",
            description = "일정의 모든 시간 슬롯에 대해 가능한 인원 수를 집계하여 반환합니다. Heatmap 표시에 사용됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "403", description = "스터디 멤버만 접근 가능"),
            @ApiResponse(responseCode = "404", description = "일정을 찾을 수 없음")
    })
    public ResponseEntity<ScheduleAvailabilitySummaryResponse> getAvailabilitySummary(
            @Parameter(description = "일정 ID", required = true)
            @PathVariable UUID scheduleId,
            @Parameter(description = "스터디 ID", required = true)
            @RequestParam UUID studyId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(availabilityService.getAvailabilitySummary(studyId, scheduleId, userPrincipal.getUser()));
    }

    @PatchMapping("/confirm")
    @Operation(
            summary = "확정 시간 저장",
            description = "스터디장이 특정 시간 슬롯을 확정 시간으로 지정합니다. 확정된 시간은 일정의 확정 시간으로 표시됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "확정 시간 저장 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 슬롯"),
            @ApiResponse(responseCode = "403", description = "스터디장 권한 필요"),
            @ApiResponse(responseCode = "404", description = "일정 또는 슬롯을 찾을 수 없음")
    })
    public ResponseEntity<String> confirmSchedule(
            @Parameter(description = "일정 ID", required = true)
            @PathVariable UUID scheduleId,
            @Parameter(description = "스터디 ID", required = true)
            @RequestParam UUID studyId,
            @Valid @RequestBody ScheduleConfirmRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        scheduleService.confirmSchedule(studyId, scheduleId, request.getSlotId(), userPrincipal.getUser());
        return ResponseEntity.ok("일정이 확정되었습니다.");
    }
}

