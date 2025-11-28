package com.studiz.domain.notification.controller;

import com.studiz.domain.notification.dto.NotificationResponse;
import com.studiz.domain.notification.service.NotificationService;
import com.studiz.domain.user.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "알림 API")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(
            summary = "알림 목록 조회",
            description = "사용자의 모든 알림 목록을 조회합니다. 최신순으로 정렬됩니다.\n\n" +
                    "**알림 타입**:\n" +
                    "- `MEMBER_INVITED`: 멤버 초대됨\n" +
                    "- `TODO_CREATED`: Todo 생성됨\n" +
                    "- `TODO_DUE_SOON`: 마감 하루 전 알림\n" +
                    "- `SCHEDULE_CONFIRMED`: 스케줄 확정됨"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = NotificationResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 필요")
    })
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(notificationService.getNotifications(userPrincipal.getUser()));
    }

    @GetMapping("/unread")
    @Operation(
            summary = "읽지 않은 알림 조회",
            description = "사용자의 읽지 않은 알림만 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요")
    })
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userPrincipal.getUser()));
    }

    @GetMapping("/unread/count")
    @Operation(
            summary = "읽지 않은 알림 개수 조회",
            description = "사용자의 읽지 않은 알림 개수를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요")
    })
    public ResponseEntity<Long> getUnreadCount(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(notificationService.getUnreadCount(userPrincipal.getUser()));
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(
            summary = "알림 읽음 처리",
            description = "특정 알림을 읽음 처리합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "읽음 처리 성공"),
            @ApiResponse(responseCode = "404", description = "알림을 찾을 수 없음"),
            @ApiResponse(responseCode = "401", description = "인증 필요")
    })
    public ResponseEntity<String> markAsRead(
            @Parameter(description = "알림 ID", required = true)
            @PathVariable UUID notificationId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        notificationService.markAsRead(notificationId, userPrincipal.getUser());
        return ResponseEntity.ok("알림이 읽음 처리되었습니다.");
    }
}

