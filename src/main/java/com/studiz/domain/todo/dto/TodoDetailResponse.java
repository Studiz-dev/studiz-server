package com.studiz.domain.todo.dto;

import com.studiz.domain.todo.entity.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "Todo 상세 정보 (참여자 포함)")
public class TodoDetailResponse {
    
    @Schema(description = "Todo ID")
    private final UUID id;

    @Schema(description = "할 일 이름")
    private final String name;

    @Schema(description = "모임명")
    private final String meetingName;

    @Schema(description = "완료 기한")
    private final LocalDateTime dueDate;

    @Schema(description = "완료율(%)")
    private final int completionRate;

    @Schema(description = "완료된 멤버 목록")
    private final List<TodoMemberResponse> completedMembers;

    @Schema(description = "미완료 멤버 목록")
    private final List<TodoMemberResponse> pendingMembers;
    
    public static TodoDetailResponse from(Todo todo) {
        TodoResponse summary = TodoResponse.from(todo);
        List<TodoMemberResponse> members = todo.getMembers()
                .stream()
                .map(TodoMemberResponse::from)
                .toList();

        return TodoDetailResponse.builder()
                .id(todo.getId())
                .name(todo.getName())
                .meetingName(todo.getStudy().getMeetingName())
                .dueDate(todo.getDueDate())
                .completionRate(summary.getCompletionRate())
                .completedMembers(members.stream().filter(TodoMemberResponse::isCompleted).toList())
                .pendingMembers(members.stream().filter(m -> !m.isCompleted()).toList())
                .build();
    }
}
