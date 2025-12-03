package com.studiz.domain.user.controller;

import com.studiz.domain.user.dto.UserProfileResponse;
import com.studiz.domain.user.dto.UserProfileUpdateRequest;
import com.studiz.domain.user.entity.User;
import com.studiz.domain.user.service.UserService;
import com.studiz.domain.user.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 프로필 API")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(
            summary = "내 정보 조회",
            description = "현재 로그인한 사용자의 프로필 정보를 조회합니다.\n\n" +
                    "**응답 내용**:\n" +
                    "- 사용자 ID\n" +
                    "- 로그인 ID\n" +
                    "- 이름"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 필요")
    })
    public ResponseEntity<UserProfileResponse> getMyProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(userService.getProfile(userPrincipal.getUser()));
    }

    @PatchMapping("/me")
    @Operation(
            summary = "프로필 수정",
            description = "현재 로그인한 사용자의 이름을 수정합니다.\n\n" +
                    "**수정 가능한 항목**:\n" +
                    "- 이름 (name)\n" +
                    "- 프로필 이미지 URL (profileImageUrl, 선택사항)\n\n" +
                    "**요청 예시**:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"name\": \"홍길동\",\n" +
                    "  \"profileImageUrl\": \"https://example.com/profile.jpg\"\n" +
                    "}\n" +
                    "```\n\n" +
                    "**주의사항**:\n" +
                    "- 로그인 ID는 변경할 수 없습니다.\n" +
                    "- 이름은 필수이며, 빈 문자열일 수 없습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 (이름이 비어있음)"),
            @ApiResponse(responseCode = "401", description = "인증 필요")
    })
    public ResponseEntity<UserProfileResponse> updateMyProfile(
            @Valid @RequestBody UserProfileUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(userService.updateProfile(
                userPrincipal.getUser(), 
                request.getName(), 
                request.getProfileImageUrl()
        ));
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "사용자 프로필 수정 (ID로)",
            description = "특정 사용자의 프로필을 수정합니다.\n\n" +
                    "**수정 가능한 항목**:\n" +
                    "- 이름 (name)\n" +
                    "- 프로필 이미지 URL (profileImageUrl, 선택사항)\n\n" +
                    "**요청 예시**:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"name\": \"홍길동\",\n" +
                    "  \"profileImageUrl\": \"https://example.com/profile.jpg\"\n" +
                    "}\n" +
                    "```\n\n" +
                    "**주의사항**:\n" +
                    "- 로그인 ID는 변경할 수 없습니다.\n" +
                    "- 이름은 필수이며, 빈 문자열일 수 없습니다.\n" +
                    "- 본인의 프로필만 수정할 수 있습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 (이름이 비어있음)"),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "403", description = "권한 없음 (본인의 프로필만 수정 가능)"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    public ResponseEntity<UserProfileResponse> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody UserProfileUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        // 본인의 프로필만 수정 가능하도록 검증
        if (!userPrincipal.getUser().getId().equals(id)) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN).build();
        }

        User user = userService.findById(id);
        return ResponseEntity.ok(userService.updateProfile(
                user, 
                request.getName(), 
                request.getProfileImageUrl()
        ));
    }
}

