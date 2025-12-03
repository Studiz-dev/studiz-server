package com.studiz.domain.todo.dto;

import com.studiz.domain.todo.entity.Todo;
import com.studiz.domain.todo.entity.TodoStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "Todo 정보")
public class TodoResponse {
    
    @Schema(description = "Todo ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private final UUID id;
    
    @Schema(description = "Todo 이름", example = "Chapter 1 문제 풀이")
    private final String name;
    
    @Schema(description = "마감일", example = "2024-01-20T23:59:59")
    private final LocalDateTime dueDate;
    
    @Schema(description = "인증 방식 배열 (TEXT_NOTE, FILE_UPLOAD 중 중복 선택 가능)", example = "[\"TEXT_NOTE\"]")
    private final List<String> certificationTypes;
    
    @Schema(description = "Todo 상태 (ACTIVE, COMPLETED)", example = "ACTIVE")
    private final TodoStatus status;
    
    @Schema(description = "완료율(%)", example = "67")
    private final int completionRate;
    
    public static TodoResponse from(Todo todo) {
        int total = todo.getMembers().size();
        int completed = (int) todo.getMembers().stream()
                .filter(member -> member.isCompleted())
                .count();
        int rate = total == 0 ? 0 : (int) Math.round((completed * 100.0) / total);
        
        return TodoResponse.builder()
                .id(todo.getId())
                .name(todo.getName())
                .dueDate(todo.getDueDate())
                .certificationTypes(todo.getCertificationTypes().stream().map(Enum::name).toList())
                .status(todo.getStatus())
                .completionRate(rate)
                .build();
    }
}
