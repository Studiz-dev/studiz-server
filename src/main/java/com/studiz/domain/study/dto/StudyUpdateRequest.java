package com.studiz.domain.study.dto;

import com.studiz.domain.study.entity.StudyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        description = "스터디 수정 요청",
        example = "{\n" +
                "  \"name\": \"Java 고급 스터디\",\n" +
                "  \"meetingName\": \"주 1회 심화 모임\",\n" +
                "  \"maxMembers\": 15,\n" +
                "  \"password\": \"newpass\",\n" +
                "  \"status\": \"ACTIVE\"\n" +
                "}"
)
public class StudyUpdateRequest {
    
    @Size(min = 2, max = 50, message = "스터디 이름은 2~50자 사이여야 합니다.")
    @Schema(description = "스터디 이름 (선택사항)", example = "Java 고급 스터디")
    private String name;
    
    @Size(min = 2, max = 100, message = "모임명은 2~100자 사이여야 합니다.")
    @Schema(description = "모임명 (선택사항)", example = "주 1회 심화 모임")
    private String meetingName;
    
    @Min(value = 1, message = "최대 인원은 1명 이상이어야 합니다.")
    @Schema(description = "최대 인원 (선택사항)", example = "15")
    private Integer maxMembers;
    
    @Size(min = 4, max = 50, message = "비밀번호는 4~50자 사이여야 합니다.")
    @Schema(description = "스터디 비밀번호 (선택사항)", example = "newpass")
    private String password;
    
    @Schema(description = "스터디 상태 (ACTIVE, INACTIVE, COMPLETED, 선택사항)", example = "ACTIVE")
    private StudyStatus status;
}
