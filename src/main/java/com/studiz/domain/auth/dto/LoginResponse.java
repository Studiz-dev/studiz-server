package com.studiz.domain.auth.dto;

import com.studiz.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private long accessTokenExpiresIn;
    private String refreshToken;
    private long refreshTokenExpiresIn;
    private String tokenType;
    
    private Long userId;
    private String loginId;
    private String name;
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
}
