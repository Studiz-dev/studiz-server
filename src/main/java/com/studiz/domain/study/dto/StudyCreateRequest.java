package com.studiz.domain.study.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Schema(
        description = "스터디 생성 요청",
        example = "{\n" +
                "  \"name\": \"Java 기초 스터디\",\n" +
                "  \"description\": \"Java 프로그래밍 기초를 함께 공부합니다.\"\n" +
                "}"
)
public class StudyCreateRequest {
    
    @NotBlank(message = "스터디 이름은 필수입니다.")
    @Size(min = 2, max = 50, message = "스터디 이름은 2~50자 사이여야 합니다.")
    @Schema(description = "스터디 이름 (2-50자)", example = "Java 기초 스터디", required = true)
    private String name;
    
    @Size(max = 500, message = "설명은 500자 이하여야 합니다.")
    @Schema(description = "스터디 설명 (선택사항, 최대 500자)", example = "Java 프로그래밍 기초를 함께 공부합니다.")
    private String description;
}
