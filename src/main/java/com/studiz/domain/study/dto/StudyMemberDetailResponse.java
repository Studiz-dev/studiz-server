package com.studiz.domain.study.dto;

import com.studiz.domain.studymember.entity.StudyMember;
import com.studiz.domain.studymember.entity.StudyMemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "스터디 멤버 상세 정보")
public class StudyMemberDetailResponse {
    
    @Schema(description = "멤버 ID (StudyMember ID)", example = "1")
    private final Long memberId;
    
    @Schema(description = "사용자 ID (User ID)", example = "1")
    private final Long userId;
    
    @Schema(description = "로그인 ID", example = "user123")
    private final String loginId;
    
    @Schema(description = "이름", example = "홍길동")
    private final String name;
    
    @Schema(description = "역할 (OWNER, MEMBER)", example = "OWNER")
    private final StudyMemberRole role;
    
    @Schema(description = "가입 일시", example = "2024-01-15T10:30:00")
    private final LocalDateTime joinedAt;
    
    @Schema(description = "스터디장 여부", example = "true")
    private final boolean owner;
    
    public static StudyMemberDetailResponse from(StudyMember member) {
        return StudyMemberDetailResponse.builder()
                .memberId(member.getId())
                .userId(member.getUser().getId())
                .loginId(member.getUser().getLoginId())
                .name(member.getUser().getName())
                .role(member.getRole())
                .joinedAt(member.getJoinedAt())
                .owner(member.getRole() == StudyMemberRole.OWNER)
                .build();
    }
}

