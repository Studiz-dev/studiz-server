package com.studiz.domain.todo.dto;

import com.studiz.domain.todo.entity.Todo;
import com.studiz.domain.todo.entity.TodoStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class TodoResponse {
    
    private final UUID id;
    private final String name;
    private final String description;
    private final LocalDateTime dueDate;
    private final String certificationType;
    private final TodoStatus status;
    private final int completedCount;
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

