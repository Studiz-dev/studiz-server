package com.studiz.domain.notification.dto;

import com.studiz.domain.notification.entity.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "알림 정보")
public class NotificationResponse {

    @Schema(description = "알림 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private final UUID id;

    @Schema(description = "알림 타입", example = "TODO_CREATED")
    private final String type;

    @Schema(description = "알림 제목", example = "새로운 할 일이 생성되었습니다")
    private final String title;

    @Schema(description = "알림 내용", example = "스터디 'Java 기초'에 새로운 할 일이 추가되었습니다.")
    private final String content;

    @Schema(description = "읽음 여부", example = "false")
    private final boolean read;

    @Schema(description = "관련 엔티티 ID (studyId, todoId, scheduleId 등)", example = "123e4567-e89b-12d3-a456-426614174001", nullable = true)
    private final UUID relatedId;

    @Schema(description = "생성 시간")
    private final LocalDateTime createdAt;

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType().name())
                .title(notification.getTitle())
                .content(notification.getContent())
                .read(notification.isRead())
                .relatedId(notification.getRelatedId())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}










