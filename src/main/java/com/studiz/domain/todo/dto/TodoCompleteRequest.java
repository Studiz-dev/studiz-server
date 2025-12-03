package com.studiz.domain.todo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        description = "Todo 완료 요청",
        example = "{\n" +
                "  \"textContent\": \"문제를 모두 풀었습니다.\",\n" +
                "  \"fileUrl\": \"https://example.com/proof.pdf\",\n" +
                "  \"reflection\": \"소감문 100자 이내\"\n" +
                "}"
)
public class TodoCompleteRequest {
    
    @Schema(description = "텍스트 인증 내용 (TEXT_NOTE가 요구될 경우 필수)", example = "문제를 모두 풀었습니다.")
    private String textContent;

    @Schema(description = "파일 인증 URL (FILE_UPLOAD가 요구될 경우 필수)", example = "https://example.com/proof.pdf")
    private String fileUrl;

    @Size(max = 100, message = "소감문은 100자 이하여야 합니다.")
    @Schema(description = "소감문 (선택, 100자 이내)", example = "과제가 유익했습니다.")
    private String reflection;
}
