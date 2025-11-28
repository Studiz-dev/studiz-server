package com.studiz.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "로그인 요청",
        example = "{\n" +
                "  \"loginId\": \"user123\",\n" +
                "  \"password\": \"Password123!\"\n" +
                "}"
)
public class LoginRequest {

    @NotBlank(message = "아이디는 필수입니다.")
    @Schema(description = "로그인 ID", example = "user123", required = true)
    private String loginId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Schema(description = "비밀번호", example = "Password123!", required = true)
    private String password;
}
