package com.studiz.domain.todo.dto;

import com.studiz.domain.todo.entity.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "Todo 상세 정보 (참여자 포함)")
public class TodoDetailResponse {
    
    @Schema(description = "Todo 기본 정보")
    private final TodoResponse todo;
    
    @Schema(description = "참여자 목록")
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

