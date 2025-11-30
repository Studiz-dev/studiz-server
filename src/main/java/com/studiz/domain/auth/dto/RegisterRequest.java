package com.studiz.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "회원가입 요청",
        example = "{\n" +
                "  \"loginId\": \"user123\",\n" +
                "  \"password\": \"Password123!\",\n" +
                "  \"name\": \"홍길동\",\n" +
                "  \"profileImageUrl\": \"https://example.com/profile.jpg\"\n" +
                "}"
)
public class RegisterRequest {

    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min = 4, max = 20, message = "아이디는 4~20자 사이여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "아이디는 영문, 숫자, 언더스코어(_), 하이픈(-)만 사용 가능합니다.")
    @Schema(description = "로그인 ID (4-20자, 영문/숫자/언더스코어/하이픈만 가능)", example = "user123", required = true)
    private String loginId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 100, message = "비밀번호는 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]+$", 
            message = "비밀번호는 대소문자, 숫자, 특수문자를 포함해야 합니다.")
    @Schema(description = "비밀번호 (8자 이상, 대소문자/숫자/특수문자 포함)", example = "Password123!", required = true)
    private String password;

    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 2, max = 50, message = "이름은 2~50자 사이여야 합니다.")
    @Schema(description = "이름 (2-50자)", example = "홍길동", required = true)
    private String name;

    @Schema(description = "프로필 이미지 URL (선택사항)", example = "https://example.com/profile.jpg", required = false)
    private String profileImageUrl;
}

