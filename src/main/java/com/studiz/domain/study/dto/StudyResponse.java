package com.studiz.domain.study.dto;

import com.studiz.domain.study.entity.Study;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class StudyResponse {
    private UUID id;
    private String name;
    private String inviteCode;
    private String description;
    private String status;
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
