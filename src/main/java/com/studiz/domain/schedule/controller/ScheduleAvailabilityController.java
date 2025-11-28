package com.studiz.domain.schedule.controller;

import com.studiz.domain.schedule.dto.ScheduleAvailabilityRequest;
import com.studiz.domain.schedule.dto.ScheduleAvailabilitySummaryResponse;
import com.studiz.domain.schedule.dto.ScheduleConfirmRequest;
import com.studiz.domain.schedule.service.ScheduleAvailabilityService;
import com.studiz.domain.schedule.service.ScheduleService;
import com.studiz.domain.user.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/schedules/{scheduleId}")
@RequiredArgsConstructor
public class ScheduleAvailabilityController {

    private final ScheduleAvailabilityService availabilityService;
    private final ScheduleService scheduleService;

    @PostMapping("/availability")
    public ResponseEntity<String> updateAvailability(
            @PathVariable UUID scheduleId,
            @RequestParam UUID studyId,
            @Valid @RequestBody ScheduleAvailabilityRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        availabilityService.updateAvailability(studyId, scheduleId, request, userPrincipal.getUser());
        return ResponseEntity.ok("가능 시간이 등록되었습니다.");
    }

    @GetMapping("/availability/summary")
    public ResponseEntity<ScheduleAvailabilitySummaryResponse> getAvailabilitySummary(
            @PathVariable UUID scheduleId,
            @RequestParam UUID studyId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(availabilityService.getAvailabilitySummary(studyId, scheduleId, userPrincipal.getUser()));
    }

    @PatchMapping("/confirm")
    public ResponseEntity<String> confirmSchedule(
            @PathVariable UUID scheduleId,
            @RequestParam UUID studyId,
            @Valid @RequestBody ScheduleConfirmRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        scheduleService.confirmSchedule(studyId, scheduleId, request.getSlotId(), userPrincipal.getUser());
        return ResponseEntity.ok("일정이 확정되었습니다.");
    }
}

