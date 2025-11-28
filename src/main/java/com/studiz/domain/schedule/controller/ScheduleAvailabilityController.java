package com.studiz.domain.schedule.controller;

import com.studiz.domain.schedule.dto.ScheduleAvailabilityRequest;
import com.studiz.domain.schedule.dto.ScheduleAvailabilitySummaryResponse;
import com.studiz.domain.schedule.dto.ScheduleConfirmRequest;
import com.studiz.domain.schedule.service.ScheduleAvailabilityService;
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
            description = "사용자가 가능한 시간 슬롯을 등록합니다.\n\n" +
                    "**동작 방식**:\n" +
                    "- 요청에 포함된 슬롯 ID들은 '가능'으로 표시됩니다.\n" +
                    "- 요청에 포함되지 않은 슬롯들은 '불가능'으로 처리됩니다.\n" +
                    "- 기존에 등록한 시간이 있으면 자동으로 업데이트됩니다.\n\n" +
                    "**요청 예시**:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"availableSlotIds\": [\n" +
                    "    \"123e4567-e89b-12d3-a456-426614174002\",\n" +
                    "    \"123e4567-e89b-12d3-a456-426614174003\",\n" +
                    "    \"123e4567-e89b-12d3-a456-426614174004\"\n" +
                    "  ]\n" +
                    "}\n" +
                    "```\n\n" +
                    "**사용 시나리오**:\n" +
                    "1. 일정 상세 조회 API로 슬롯 목록을 가져옵니다.\n" +
                    "2. 사용자가 UI에서 가능한 시간을 선택합니다.\n" +
                    "3. 선택한 슬롯 ID 리스트를 이 API로 전송합니다.\n\n" +
                    "**주의사항**:\n" +
                    "- `availableSlotIds`는 최소 1개 이상이어야 합니다.\n" +
                    "- 슬롯 ID는 해당 일정의 슬롯 ID여야 합니다.\n" +
                    "- URL에 `studyId`를 쿼리 파라미터로 전달해야 합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "가능 시간 등록 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 (빈 리스트 등)"),
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
            description = "일정의 모든 시간 슬롯에 대해 가능한 인원 수를 집계하여 반환합니다.\n\n" +
                    "**용도**:\n" +
                    "- Heatmap 표시에 사용됩니다.\n" +
                    "- 각 슬롯별로 몇 명이 가능한지 확인할 수 있습니다.\n\n" +
                    "**응답 구조**:\n" +
                    "- `slots`: 슬롯별 가능 인원 수 리스트\n" +
                    "  - `slotId`: 시간 슬롯 ID\n" +
                    "  - `availableCount`: 해당 슬롯에 가능하다고 표시한 인원 수\n\n" +
                    "**사용 예시**:\n" +
                    "- `availableCount`가 높을수록 많은 사람이 가능한 시간입니다.\n" +
                    "- 모든 멤버 수와 비교하여 확정 시간을 결정할 수 있습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ScheduleAvailabilitySummaryResponse.class))),
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
            description = "스터디장이 특정 시간 슬롯을 확정 시간으로 지정합니다.\n\n" +
                    "**권한**: 스터디장만 가능\n\n" +
                    "**동작**:\n" +
                    "- 지정한 슬롯이 일정의 확정 시간으로 저장됩니다.\n" +
                    "- 일정 조회 시 `confirmedSlotId`에 표시됩니다.\n" +
                    "- 한 번에 하나의 슬롯만 확정할 수 있습니다.\n" +
                    "- 확정 시 모든 스터디 멤버에게 알림이 전송됩니다.\n\n" +
                    "**요청 예시**:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"slotId\": \"123e4567-e89b-12d3-a456-426614174002\"\n" +
                    "}\n" +
                    "```\n\n" +
                    "**사용 시나리오**:\n" +
                    "1. 가능 시간 통계 API로 각 슬롯별 가능 인원 수를 확인합니다.\n" +
                    "2. 가장 많은 인원이 가능한 슬롯을 선택합니다.\n" +
                    "3. 해당 슬롯 ID를 이 API로 전송하여 확정합니다.\n\n" +
                    "**주의사항**:\n" +
                    "- 슬롯 ID는 해당 일정의 슬롯이어야 합니다.\n" +
                    "- 다른 일정의 슬롯 ID를 사용하면 오류가 발생합니다.\n" +
                    "- URL에 `studyId`를 쿼리 파라미터로 전달해야 합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "확정 시간 저장 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 슬롯 (다른 일정의 슬롯 등)"),
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

