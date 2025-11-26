package com.studiz.domain.todo.controller;

import com.studiz.domain.todo.dto.TodoCompleteRequest;
import com.studiz.domain.todo.dto.TodoCreateRequest;
import com.studiz.domain.todo.dto.TodoDetailResponse;
import com.studiz.domain.todo.dto.TodoResponse;
import com.studiz.domain.todo.service.TodoService;
import com.studiz.domain.user.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/studies/{studyId}/todos")
@RequiredArgsConstructor
public class TodoController {
    
    private final TodoService todoService;
    
    @PostMapping
    public ResponseEntity<TodoDetailResponse> createTodo(
            @PathVariable UUID studyId,
            @Valid @RequestBody TodoCreateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(todoService.createTodo(studyId, request, userPrincipal.getUser()));
    }
    
    @GetMapping
    public ResponseEntity<List<TodoResponse>> getTodos(
            @PathVariable UUID studyId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(todoService.getTodos(studyId, userPrincipal.getUser()));
    }
    
    @GetMapping("/{todoId}")
    public ResponseEntity<TodoDetailResponse> getTodoDetail(
            @PathVariable UUID studyId,
            @PathVariable UUID todoId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(todoService.getTodoDetail(studyId, todoId, userPrincipal.getUser()));
    }
    
    @PatchMapping("/{todoId}/complete")
    public ResponseEntity<String> completeTodo(
            @PathVariable UUID studyId,
            @PathVariable UUID todoId,
            @Valid @RequestBody TodoCompleteRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        todoService.completeTodo(studyId, todoId, userPrincipal.getUser(), request);
        return ResponseEntity.ok("할 일이 완료되었습니다.");
    }
}

