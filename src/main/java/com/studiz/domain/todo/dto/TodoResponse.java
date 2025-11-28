package com.studiz.domain.todo.dto;

import com.studiz.domain.todo.entity.Todo;
import com.studiz.domain.todo.entity.TodoStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "Todo 정보")
public class TodoResponse {
    
    @Schema(description = "Todo ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private final UUID id;
    
    @Schema(description = "Todo 이름", example = "Chapter 1 문제 풀이")
    private final String name;
    
    @Schema(description = "Todo 설명", example = "Java 기초 Chapter 1 문제를 풀고 제출하세요.")
    private final String description;
    
    @Schema(description = "마감일", example = "2024-01-20T23:59:59")
    private final LocalDateTime dueDate;
    
    @Schema(description = "인증 방식 (TEXT_NOTE, FILE_UPLOAD)", example = "TEXT_NOTE")
    private final String certificationType;
    
    @Schema(description = "Todo 상태 (ACTIVE, COMPLETED)", example = "ACTIVE")
    private final TodoStatus status;
    
    @Schema(description = "완료된 참여자 수", example = "2")
    private final int completedCount;
    
    @Schema(description = "전체 참여자 수", example = "3")
    private final int totalCount;
    
    public static TodoResponse from(Todo todo) {
        int total = todo.getMembers().size();
        int completed = (int) todo.getMembers().stream()
                .filter(member -> member.isCompleted())
                .count();
        
        return TodoResponse.builder()
                .id(todo.getId())
                .name(todo.getName())
                .description(todo.getDescription())
                .dueDate(todo.getDueDate())
                .certificationType(todo.getCertificationType().name())
                .status(todo.getStatus())
                .completedCount(completed)
                .totalCount(total)
                .build();
    }
}

