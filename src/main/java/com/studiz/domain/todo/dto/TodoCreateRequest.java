package com.studiz.domain.todo.dto;

import com.studiz.domain.todo.entity.TodoCertificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(
        description = "Todo 생성 요청",
        example = "{\n" +
                "  \"name\": \"Chapter 1 문제 풀이\",\n" +
                "  \"dueDate\": \"2024-01-20T23:59:59\",\n" +
                "  \"certificationTypes\": [\"TEXT_NOTE\", \"FILE_UPLOAD\"],\n" +
                "  \"participantIds\": [1, 2, 3]\n" +
                "}"
)
public class TodoCreateRequest {
    
    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 100, message = "이름은 100자 이하여야 합니다.")
    @Schema(description = "Todo 이름 (최대 100자)", example = "Chapter 1 문제 풀이", required = true)
    private String name;
    
    @NotNull(message = "마감일은 필수입니다.")
    @Future(message = "마감일은 현재 이후여야 합니다.")
    @Schema(description = "마감일 (현재 이후여야 함, ISO 형식)", example = "2024-01-20T23:59:59", required = true)
    private LocalDateTime dueDate;
    
    @NotEmpty(message = "인증 방식은 최소 하나 이상 선택해야 합니다.")
    @Schema(description = "인증 방식 배열 (TEXT_NOTE, FILE_UPLOAD 중 중복 선택 가능)", example = "[\"TEXT_NOTE\", \"FILE_UPLOAD\"]", required = true)
    private List<TodoCertificationType> certificationTypes;
    
    @NotEmpty(message = "참여자는 최소 1명 이상이어야 합니다.")
    @Schema(description = "참여자 사용자 ID 리스트 (최소 1명 이상)", example = "[1, 2, 3]", required = true)
    private List<Long> participantIds;
}
