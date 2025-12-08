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
                    "**인증**: Bearer Token 필요\n\n" +
                    "**권한**: 스터디장만 생성 가능\n\n" +
                    "**동작 방식**:\n" +
                    "- 지정한 날짜에 대해 1시간 단위 시간 슬롯이 자동 생성됩니다.\n" +
                    "- 예: 2024-01-15이면, 해당 날짜의 00:00부터 23:00까지 24개의 슬롯이 생성됩니다.\n" +
                    "- 생성된 슬롯은 일정 상세 조회 API로 확인할 수 있습니다.\n" +
                    "- 스터디장이 시간을 확정하면 일정이 완료됩니다.\n\n" +
                    "**요청 필드**:\n" +
                    "- `title`: 일정 제목 (필수)\n" +
                    "- `startDate`: 일정 날짜 (필수, 형식: `YYYY-MM-DD`)\n" +
                    "- `location`: 장소 (선택사항)\n\n" +
                    "**요청 예시**:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"title\": \"스터디 모임 일정 조율\",\n" +
                    "  \"startDate\": \"2024-01-15\",\n" +
                    "  \"location\": \"서울시 강남구\"\n" +
                    "}\n" +
                    "```\n\n" +
                    "**주의사항**:\n" +
                    "- 스터디장이 아닌 경우 403 에러가 반환됩니다.\n" +
                    "- 존재하지 않는 스터디 ID인 경우 404 에러가 반환됩니다."
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
                    "**인증**: Bearer Token 필요\n\n" +
                    "**응답 내용**:\n" +
                    "- 일정 배열 (각 일정마다 다음 정보 포함)\n" +
                    "  - `id`: 일정 ID (UUID)\n" +
                    "  - `title`: 일정 제목\n" +
                    "  - `startDate`: 일정 날짜\n" +
                    "  - `location`: 장소\n" +
                    "  - `confirmedSlotId`: 확정된 시간 슬롯 ID (확정되지 않았으면 null)\n" +
                    "  - `confirmedStartTime`: 확정된 시작 시간 (확정되지 않았으면 null)\n" +
                    "  - `confirmedEndTime`: 확정된 종료 시간 (확정되지 않았으면 null)\n\n" +
                    "**정렬**: 확정된 일정 우선, 그 다음 날짜 오름차순\n\n" +
                    "**권한**: 스터디 멤버만 접근 가능\n\n" +
                    "**주의사항**:\n" +
                    "- 스터디에 가입하지 않은 사용자는 접근할 수 없습니다 (403 에러).\n" +
                    "- 존재하지 않는 스터디 ID인 경우 404 에러가 반환됩니다."
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
            summary = "일정 상세 조회",
            description = "특정 일정의 상세 정보를 조회합니다.\n\n" +
                    "**인증**: Bearer Token 필요\n\n" +
                    "**응답 내용**:\n" +
                    "- 일정 기본 정보 (제목, 시작일, 장소)\n" +
                    "- 확정된 시간 슬롯 ID (확정되지 않았으면 null)\n" +
                    "- 모든 시간 슬롯 목록 (시작 시간 순으로 정렬)\n" +
                    "  - 각 슬롯은 1시간 단위입니다.\n" +
                    "  - `startTime`과 `endTime`으로 시간 범위를 확인할 수 있습니다.\n" +
                    "  - 각 슬롯의 가능 여부를 확인할 수 있습니다.\n\n" +
                    "**권한**: 스터디 멤버만 접근 가능\n\n" +
                    "**주의사항**:\n" +
                    "- 스터디에 가입하지 않은 사용자는 접근할 수 없습니다 (403 에러).\n" +
                    "- 존재하지 않는 일정 ID인 경우 404 에러가 반환됩니다."
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

