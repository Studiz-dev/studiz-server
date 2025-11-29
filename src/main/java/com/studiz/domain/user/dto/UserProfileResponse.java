package com.studiz.domain.user.dto;

import com.studiz.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "사용자 프로필 정보")
public class UserProfileResponse {

    @Schema(description = "사용자 ID", example = "1")
    private final Long id;

    @Schema(description = "로그인 ID", example = "user123")
    private final String loginId;

    @Schema(description = "이름", example = "홍길동")
    private final String name;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg", nullable = true)
    private final String profileImageUrl;

    public static UserProfileResponse from(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .name(user.getName())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}


