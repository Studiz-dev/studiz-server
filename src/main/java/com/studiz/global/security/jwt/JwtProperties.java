package com.studiz.global.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * JWT 서명에 사용되는 시크릿 키
     */
    private String secret;

    /**
     * 액세스 토큰 만료 시간 (millisecond)
     */
    private long accessTokenExpiration;

    /**
     * 리프레시 토큰 만료 시간 (millisecond)
     */
    private long refreshTokenExpiration;
}

