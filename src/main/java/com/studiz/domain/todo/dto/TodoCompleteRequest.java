package com.studiz.domain.todo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        description = "Todo 완료 요청",
        example = "{\n" +
                "  \"content\": \"문제를 모두 풀었습니다. 코드 리뷰 완료.\"\n" +
                "}"
)
public class TodoCompleteRequest {
    
    @NotBlank(message = "인증 내용은 필수입니다.")
    @Schema(description = "인증 내용 (텍스트 인증의 경우 텍스트, 파일 업로드의 경우 파일 URL 등)", example = "문제를 모두 풀었습니다. 코드 리뷰 완료.", required = true)
    private String content;
}
