package com.studiz.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(
        description = "사용자 프로필 수정 요청",
        example = "{\n" +
                "  \"name\": \"홍길동\",\n" +
                "  \"profileImageUrl\": \"https://example.com/profile.jpg\"\n" +
                "}"
)
public class UserProfileUpdateRequest {

    @NotBlank(message = "이름은 필수입니다.")
    @Schema(description = "변경할 이름", example = "홍길동", required = true)
    private String name;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg", nullable = true)
    private String profileImageUrl;
}

