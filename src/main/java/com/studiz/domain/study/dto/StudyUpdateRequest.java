package com.studiz.domain.study.dto;

import com.studiz.domain.study.entity.StudyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        description = "스터디 수정 요청",
        example = "{\n" +
                "  \"name\": \"Java 고급 스터디\",\n" +
                "  \"description\": \"Java 고급 프로그래밍을 함께 공부합니다.\",\n" +
                "  \"status\": \"ACTIVE\"\n" +
                "}"
)
public class StudyUpdateRequest {
    
    @Size(min = 2, max = 50, message = "스터디 이름은 2~50자 사이여야 합니다.")
    @Schema(description = "스터디 이름 (2-50자, 선택사항)", example = "Java 고급 스터디")
    private String name;
    
    @Size(max = 500, message = "설명은 500자 이하여야 합니다.")
    @Schema(description = "스터디 설명 (선택사항, 최대 500자)", example = "Java 고급 프로그래밍을 함께 공부합니다.")
    private String description;
    
    @Schema(description = "스터디 상태 (ACTIVE, INACTIVE, COMPLETED, 선택사항)", example = "ACTIVE")
    private StudyStatus status;
}
