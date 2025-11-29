package com.studiz.domain.auth.dto;

import com.studiz.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "회원가입 응답")
public class RegisterResponse {

    @Schema(description = "사용자 ID", example = "1")
    private Long id;

    @Schema(description = "로그인 ID", example = "user123")
    private String loginId;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg", nullable = true)
    private String profileImageUrl;

    @Schema(description = "가입 일시", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    public static RegisterResponse from(User user) {
        return RegisterResponse.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .name(user.getName())
                .profileImageUrl(user.getProfileImageUrl())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

