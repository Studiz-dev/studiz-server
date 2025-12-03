package com.studiz.domain.study.dto;

import com.studiz.domain.study.entity.Study;
import com.studiz.domain.studymember.entity.StudyMember;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "스터디 상세 정보 (멤버 포함)")
public class StudyDetailResponse {
    
    @Schema(description = "스터디 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private final UUID id;
    
    @Schema(description = "스터디 이름", example = "Java 기초 스터디")
    private final String name;
    
    @Schema(description = "모임명", example = "주 2회 실시간 모임")
    private final String meetingName;
    
    @Schema(description = "최대 인원", example = "10")
    private final Integer maxMembers;
    
    @Schema(description = "스터디 상태 (ACTIVE, INACTIVE, COMPLETED)", example = "ACTIVE")
    private final String status;
    
    @Schema(description = "초대코드 (8자리)", example = "abc12345")
    private final String inviteCode;
    
    @Schema(description = "생성 일시", example = "2024-01-15T10:30:00")
    private final LocalDateTime createdAt;
    
    @Schema(description = "수정 일시", example = "2024-01-15T10:30:00")
    private final LocalDateTime updatedAt;
    
    @Schema(description = "멤버 목록")
    private final List<StudyMemberDetailResponse> members;
    
    public static StudyDetailResponse from(Study study, List<StudyMember> members) {
        return StudyDetailResponse.builder()
                .id(study.getId())
                .name(study.getName())
                .meetingName(study.getMeetingName())
                .maxMembers(study.getMaxMembers())
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
