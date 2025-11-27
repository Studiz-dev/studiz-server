package com.studiz.domain.todo.dto;

import com.studiz.domain.todo.entity.TodoMember;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TodoMemberResponse {
    
    private final Long memberId;
    private final Long userId;
    private final String loginId;
    private final String name;
    private final boolean completed;
    private final LocalDateTime completedAt;
    private final String certificationContent;
    
    public static TodoMemberResponse from(TodoMember member) {
        return TodoMemberResponse.builder()
                .memberId(member.getId())
                .userId(member.getUser().getId())
                .loginId(member.getUser().getLoginId())
                .name(member.getUser().getName())
                .completed(member.isCompleted())
                .completedAt(member.getCompletedAt())
                .certificationContent(member.getCertificationContent())
                .build();
    }
}

