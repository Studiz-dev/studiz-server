package com.studiz.domain.auth.service;

import com.studiz.domain.auth.dto.LoginRequest;
import com.studiz.domain.auth.dto.LoginResponse;
import com.studiz.domain.user.security.UserPrincipal;
import com.studiz.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        log.info("로그인 성공: loginId={}", principal.getUsername());

        String accessToken = jwtTokenProvider.generateAccessToken(principal.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(principal.getUsername());

        return LoginResponse.of(
                principal.getUser(),
                accessToken,
                jwtTokenProvider.getAccessTokenValidity(),
                refreshToken,
                jwtTokenProvider.getRefreshTokenValidity(),
                "Bearer"
        );
    }
}
