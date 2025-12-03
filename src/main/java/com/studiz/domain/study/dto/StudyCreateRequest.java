package com.studiz.domain.study.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Schema(
        description = "스터디 생성 요청",
        example = "{\n" +
                "  \"name\": \"Java 기초 스터디\",\n" +
                "  \"meetingName\": \"주 2회 실시간 모임\",\n" +
                "  \"maxMembers\": 10,\n" +
                "  \"password\": \"study1234\"\n" +
                "}"
)
public class StudyCreateRequest {
    
    @NotBlank(message = "스터디 이름은 필수입니다.")
    @Size(min = 2, max = 50, message = "스터디 이름은 2~50자 사이여야 합니다.")
    @Schema(description = "스터디 이름 (2-50자)", example = "Java 기초 스터디", required = true)
    private String name;
    
    @NotBlank(message = "모임명은 필수입니다.")
    @Size(min = 2, max = 100, message = "모임명은 2~100자 사이여야 합니다.")
    @Schema(description = "모임명", example = "주 2회 실시간 모임", required = true)
    private String meetingName;
    
    @NotNull(message = "최대 인원은 필수입니다.")
    @Min(value = 1, message = "최대 인원은 1명 이상이어야 합니다.")
    @Schema(description = "최대 인원", example = "10", required = true)
    private Integer maxMembers;
    
    @NotBlank(message = "스터디 비밀번호는 필수입니다.")
    @Size(min = 4, max = 50, message = "비밀번호는 4~50자 사이여야 합니다.")
    @Schema(description = "스터디 비밀번호", example = "study1234", required = true)
    private String password;
}
