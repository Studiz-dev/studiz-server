package com.studiz.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private final String accessToken;
    private final long accessTokenExpiresIn;
    private final String refreshToken;
    private final long refreshTokenExpiresIn;
    private final String tokenType;
}

