package com.studiz.domain.schedule.controller;

import com.studiz.domain.schedule.dto.ScheduleCreateRequest;
import com.studiz.domain.schedule.dto.ScheduleDetailResponse;
import com.studiz.domain.schedule.dto.ScheduleResponse;
import com.studiz.domain.schedule.service.ScheduleService;
import com.studiz.domain.user.security.UserPrincipal;
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
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(
            @PathVariable UUID studyId,
            @Valid @RequestBody ScheduleCreateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(scheduleService.createSchedule(studyId, request, userPrincipal.getUser()));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getSchedules(
            @PathVariable UUID studyId,
            @RequestParam(required = false) String month,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(scheduleService.getSchedules(studyId, month, userPrincipal.getUser()));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDetailResponse> getScheduleDetail(
            @PathVariable UUID studyId,
            @PathVariable UUID scheduleId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(scheduleService.getScheduleDetail(studyId, scheduleId, userPrincipal.getUser()));
    }
}

