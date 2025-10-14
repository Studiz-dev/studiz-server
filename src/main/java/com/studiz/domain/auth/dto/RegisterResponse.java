package com.studiz.domain.auth.dto;

import com.studiz.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RegisterResponse {

    private Long id;
    private String loginId;
    private String name;
    private LocalDateTime createdAt;

    public static RegisterResponse from(User user) {
        return RegisterResponse.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .name(user.getName())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

