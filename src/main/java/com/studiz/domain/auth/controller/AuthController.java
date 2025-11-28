package com.studiz.domain.auth.controller;

import com.studiz.domain.auth.dto.LoginRequest;
import com.studiz.domain.auth.dto.LoginResponse;
import com.studiz.domain.auth.dto.RegisterRequest;
import com.studiz.domain.auth.dto.RegisterResponse;
import com.studiz.domain.auth.service.AuthService;
import com.studiz.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "회원가입",
            description = "새로운 사용자를 등록합니다.\n\n" +
                    "**요청 예시**:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"loginId\": \"user123\",\n" +
                    "  \"password\": \"Password123!\",\n" +
                    "  \"name\": \"홍길동\"\n" +
                    "}\n" +
                    "```\n\n" +
                    "**유효성 검사**:\n" +
                    "- `loginId`: 4-20자, 영문/숫자/언더스코어/하이픈만 가능\n" +
                    "- `password`: 8자 이상, 대소문자/숫자/특수문자 포함 필수\n" +
                    "- `name`: 2-50자\n\n" +
                    "**응답**:\n" +
                    "- 생성된 사용자 정보 반환 (HTTP 201)\n\n" +
                    "**에러 케이스**:\n" +
                    "- 409: 이미 사용 중인 아이디"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = RegisterResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효성 검사 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 사용 중인 아이디")
    })
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("회원가입 요청: loginId={}", request.getLoginId());
        RegisterResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/login")
    @Operation(
            summary = "로그인",
            description = "로그인 성공 시 JWT 토큰을 발급합니다.\n\n" +
                    "**요청 예시**:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"loginId\": \"user123\",\n" +
                    "  \"password\": \"Password123!\"\n" +
                    "}\n" +
                    "```\n\n" +
                    "**응답 내용**:\n" +
                    "- `accessToken`: API 호출 시 사용할 JWT 토큰\n" +
                    "- `refreshToken`: Access Token 갱신용 토큰\n" +
                    "- `tokenType`: \"Bearer\" (Authorization 헤더에 사용)\n" +
                    "- 사용자 정보 (userId, loginId, name)\n\n" +
                    "**토큰 사용 방법**:\n" +
                    "```\n" +
                    "Authorization: Bearer {accessToken}\n" +
                    "```\n\n" +
                    "**에러 케이스**:\n" +
                    "- 401: 잘못된 비밀번호\n" +
                    "- 404: 존재하지 않는 아이디"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = LoginResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "잘못된 비밀번호"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 아이디")
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("로그인 요청: loginId={}", request.getLoginId());
        return ResponseEntity.ok(authService.login(request));
    }
}
