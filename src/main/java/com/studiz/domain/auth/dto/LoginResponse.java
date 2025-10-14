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

    private Long userId;
    private String loginId;
    private String name;
    private String message;

    public static LoginResponse from(User user) {
        return LoginResponse.builder()
                .userId(user.getId())
                .loginId(user.getLoginId())
                .name(user.getName())
                .message("로그인 성공")
                .build();
    }
}

