package com.studiz.domain.study.dto;

import com.studiz.domain.study.entity.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "스터디 정보")
public class StudyResponse {
    @Schema(description = "스터디 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
    
    @Schema(description = "스터디 이름", example = "Java 기초 스터디")
    private String name;
    
    @Schema(description = "초대코드 (8자리)", example = "abc12345")
    private String inviteCode;
    
    @Schema(description = "스터디 설명", example = "Java 프로그래밍 기초를 함께 공부합니다.")
    private String description;
    
    @Schema(description = "스터디 상태 (ACTIVE, INACTIVE, COMPLETED)", example = "ACTIVE")
    private String status;
    
    @Schema(description = "생성 일시", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    public static StudyResponse from(Study study) {
        return StudyResponse.builder()
                .id(study.getId())
                .name(study.getName())
                .inviteCode(study.getInviteCode())
                .description(study.getDescription())
                .status(study.getStatus().name())
                .createdAt(study.getCreatedAt())
                .build();
    }
}
