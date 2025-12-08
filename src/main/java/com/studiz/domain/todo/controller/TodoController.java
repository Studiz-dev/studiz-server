package com.studiz.domain.todo.controller;

import com.studiz.domain.todo.dto.TodoCompleteRequest;
import com.studiz.domain.todo.dto.TodoCreateRequest;
import com.studiz.domain.todo.dto.TodoDetailResponse;
import com.studiz.domain.todo.dto.TodoResponse;
import com.studiz.domain.todo.service.TodoService;
import com.studiz.domain.user.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Todo", description = "할 일 관리 API")
public class TodoController {
    
    private final TodoService todoService;
    
    @PostMapping
    @Operation(
            summary = "Todo 생성",
            description = "스터디에 새로운 할 일을 생성합니다.\n\n" +
                    "**인증**: Bearer Token 필요\n\n" +
                    "**권한**: 스터디장만 생성 가능\n\n" +
                    "**요청 필드**:\n" +
                    "- `name`: 할 일 이름 (필수)\n" +
                    "- `dueDate`: 마감일 (필수, ISO 8601 형식: `YYYY-MM-DDTHH:mm:ss`)\n" +
                    "- `certificationTypes`: 인증 방식 배열 (필수, 최소 1개)\n" +
                    "- `participantIds`: 참여자 ID 배열 (필수, 최소 1명)\n\n" +
                    "**요청 예시**:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"name\": \"Chapter 1 문제 풀이\",\n" +
                    "  \"dueDate\": \"2024-01-20T23:59:59\",\n" +
                    "  \"certificationTypes\": [\"TEXT_NOTE\", \"FILE_UPLOAD\"],\n" +
                    "  \"participantIds\": [1, 2, 3]\n" +
                    "}\n" +
                    "```\n\n" +
                    "**인증 방식 (certificationTypes)**:\n" +
                    "- `TEXT_NOTE`: 텍스트 인증 (완료 시 텍스트 입력)\n" +
                    "- `FILE_UPLOAD`: 파일 업로드 인증\n" +
                    "- 두 방식을 함께 선택할 수 있습니다.\n\n" +
                    "**주의사항**:\n" +
                    "- `participantIds`에 포함된 사용자는 모두 해당 스터디의 멤버여야 합니다.\n" +
                    "- 참여자 알림은 전송되지 않습니다.\n" +
                    "- 스터디장이 아닌 경우 403 에러가 반환됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Todo 생성 성공", content = @Content(schema = @Schema(implementation = TodoDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패 또는 유효하지 않은 참여자"),
            @ApiResponse(responseCode = "403", description = "스터디장 권한 필요"),
            @ApiResponse(responseCode = "404", description = "스터디를 찾을 수 없음")
    })
    public ResponseEntity<TodoDetailResponse> createTodo(
            @Parameter(description = "스터디 ID", required = true)
            @PathVariable UUID studyId,
            @Valid @RequestBody TodoCreateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(todoService.createTodo(studyId, request, userPrincipal.getUser()));
    }
    
    @GetMapping
    @Operation(
            summary = "Todo 목록 조회",
            description = "스터디의 모든 할 일 목록을 조회합니다. 마감일 순으로 정렬됩니다.\n\n" +
                    "**인증**: Bearer Token 필요\n\n" +
                    "**응답 내용**:\n" +
                    "- Todo 배열 (각 Todo마다 다음 정보 포함)\n" +
                    "  - `id`: Todo ID (UUID)\n" +
                    "  - `name`: 할 일 이름\n" +
                    "  - `dueDate`: 마감일\n" +
                    "  - `certificationTypes`: 인증 방식 배열\n" +
                    "  - `status`: 완료 상태 (COMPLETED, IN_PROGRESS)\n" +
                    "  - `completedCount`: 완료된 참여자 수\n" +
                    "  - `totalCount`: 전체 참여자 수\n\n" +
                    "**권한**: 스터디 멤버만 접근 가능\n\n" +
                    "**주의사항**:\n" +
                    "- 스터디에 가입하지 않은 사용자는 접근할 수 없습니다 (403 에러).\n" +
                    "- 존재하지 않는 스터디 ID인 경우 404 에러가 반환됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = TodoResponse.class))),
            @ApiResponse(responseCode = "403", description = "스터디 멤버만 접근 가능"),
            @ApiResponse(responseCode = "404", description = "스터디를 찾을 수 없음")
    })
    public ResponseEntity<List<TodoResponse>> getTodos(
            @Parameter(description = "스터디 ID", required = true)
            @PathVariable UUID studyId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(todoService.getTodos(studyId, userPrincipal.getUser()));
    }
    
    @GetMapping("/{todoId}")
    @Operation(
            summary = "Todo 상세 조회",
            description = "특정 할 일의 상세 정보를 조회합니다.\n\n" +
                    "**인증**: Bearer Token 필요\n\n" +
                    "**응답 내용**:\n" +
                    "- Todo 기본 정보 (이름, 마감일, 인증 방식, 완료율 등)\n" +
                    "- 완료 멤버 목록 (각 멤버의 인증 내용 포함)\n" +
                    "- 미완료 멤버 목록\n\n" +
                    "**권한**: 스터디 멤버만 접근 가능\n\n" +
                    "**주의사항**:\n" +
                    "- 스터디에 가입하지 않은 사용자는 접근할 수 없습니다 (403 에러).\n" +
                    "- 존재하지 않는 Todo ID인 경우 404 에러가 반환됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = TodoDetailResponse.class))),
            @ApiResponse(responseCode = "403", description = "스터디 멤버만 접근 가능"),
            @ApiResponse(responseCode = "404", description = "Todo를 찾을 수 없음")
    })
    public ResponseEntity<TodoDetailResponse> getTodoDetail(
            @Parameter(description = "스터디 ID", required = true)
            @PathVariable UUID studyId,
            @Parameter(description = "Todo ID", required = true)
            @PathVariable UUID todoId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(todoService.getTodoDetail(studyId, todoId, userPrincipal.getUser()));
    }
    
    @PatchMapping("/{todoId}/complete")
    @Operation(
            summary = "Todo 완료 처리",
            description = "할 일을 완료 처리합니다. 참여자만 완료할 수 있습니다.\n\n" +
                    "**인증**: Bearer Token 필요\n\n" +
                    "**권한**: 해당 Todo의 참여자만 완료 가능\n\n" +
                    "**요청 필드**:\n" +
                    "- `content`: 인증 내용 (필수)\n" +
                    "  - TEXT_NOTE 인증: 완료 소감이나 설명을 입력\n" +
                    "  - FILE_UPLOAD 인증: 파일 업로드 후 설명을 입력\n\n" +
                    "**요청 예시**:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"content\": \"문제를 모두 풀었습니다. 코드 리뷰 완료.\"\n" +
                    "}\n" +
                    "```\n\n" +
                    "**동작**:\n" +
                    "1. 현재 사용자가 해당 Todo의 참여자인지 확인합니다.\n" +
                    "2. 인증 내용을 저장하고 완료 상태로 변경합니다.\n" +
                    "3. 모든 참여자가 완료하면 Todo도 자동으로 완료 상태가 됩니다.\n\n" +
                    "**응답**:\n" +
                    "- 성공 시: `\"할 일이 완료되었습니다.\"` 메시지 반환\n\n" +
                    "**주의사항**:\n" +
                    "- 이미 완료한 Todo는 다시 완료할 수 없습니다 (400 에러).\n" +
                    "- `content`는 필수이며, 빈 문자열일 수 없습니다.\n" +
                    "- 참여자가 아닌 경우 403 에러가 반환됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "완료 처리 성공"),
            @ApiResponse(responseCode = "400", description = "이미 완료된 Todo 또는 인증 내용 누락"),
            @ApiResponse(responseCode = "403", description = "참여자가 아님"),
            @ApiResponse(responseCode = "404", description = "Todo를 찾을 수 없음")
    })
    public ResponseEntity<String> completeTodo(
            @Parameter(description = "스터디 ID", required = true)
            @PathVariable UUID studyId,
            @Parameter(description = "Todo ID", required = true)
            @PathVariable UUID todoId,
            @Valid @RequestBody TodoCompleteRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        todoService.completeTodo(studyId, todoId, userPrincipal.getUser(), request);
        return ResponseEntity.ok("할 일이 완료되었습니다.");
    }
}
