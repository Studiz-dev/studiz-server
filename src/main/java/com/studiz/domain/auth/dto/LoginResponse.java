package com.studiz.domain.auth.dto;

import com.studiz.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 응답")
public class LoginResponse {

    @Schema(description = "JWT Access Token (이후 API 호출 시 Authorization 헤더에 사용)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "Access Token 만료 시간 (밀리초)", example = "3600000")
    private long accessTokenExpiresIn;

    @Schema(description = "JWT Refresh Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    @Schema(description = "Refresh Token 만료 시간 (밀리초)", example = "604800000")
    private long refreshTokenExpiresIn;

    @Schema(description = "토큰 타입", example = "Bearer")
    private String tokenType;

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "로그인 ID", example = "user123")
    private String loginId;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "응답 메시지", example = "로그인 성공")
    private String message;

    public static LoginResponse of(User user,
                                   String accessToken,
                                   long accessTokenExpiresIn,
                                   String refreshToken,
                                   long refreshTokenExpiresIn,
                                   String tokenType) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn)
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(refreshTokenExpiresIn)
                .tokenType(tokenType)

                .userId(user.getId())
                .loginId(user.getLoginId())
                .name(user.getName())
                .message("로그인 성공")
                .build();
    }
    
    public static LoginResponse from(User user) {
        return LoginResponse.builder()
                .userId(user.getId())
                .loginId(user.getLoginId())
                .name(user.getName())
                .message("로그인 성공")
                .build();
    }
}
