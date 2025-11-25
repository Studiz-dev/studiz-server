package com.studiz.domain.study.dto;

import com.studiz.domain.studymember.entity.StudyMember;
import com.studiz.domain.studymember.entity.StudyMemberRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudyMemberDetailResponse {
    
    private final Long memberId;
    private final Long userId;
    private final String loginId;
    private final String name;
    private final StudyMemberRole role;
    private final LocalDateTime joinedAt;
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

