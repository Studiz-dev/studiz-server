package com.studiz.domain.todo.dto;

import com.studiz.domain.todo.entity.Todo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TodoDetailResponse {
    
    private final TodoResponse todo;
    private final List<TodoMemberResponse> members;
    
    public static TodoDetailResponse from(Todo todo) {
        return TodoDetailResponse.builder()
                .todo(TodoResponse.from(todo))
                .members(todo.getMembers()
                        .stream()
                        .map(TodoMemberResponse::from)
                        .toList())
                .build();
    }
}

