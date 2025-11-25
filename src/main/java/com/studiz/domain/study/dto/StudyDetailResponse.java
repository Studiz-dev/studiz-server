package com.studiz.domain.study.dto;

import com.studiz.domain.study.entity.Study;
import com.studiz.domain.studymember.entity.StudyMember;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class StudyDetailResponse {
    
    private final UUID id;
    private final String name;
    private final String description;
    private final String status;
    private final String inviteCode;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<StudyMemberDetailResponse> members;
    
    public static StudyDetailResponse from(Study study, List<StudyMember> members) {
        return StudyDetailResponse.builder()
                .id(study.getId())
                .name(study.getName())
                .description(study.getDescription())
                .status(study.getStatus().name())
                .inviteCode(study.getInviteCode())
                .createdAt(study.getCreatedAt())
                .updatedAt(study.getUpdatedAt())
                .members(members.stream()
                        .map(StudyMemberDetailResponse::from)
                        .toList())
                .build();
    }
}

