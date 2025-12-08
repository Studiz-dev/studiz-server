package com.studiz.domain.study.controller;

import com.studiz.domain.study.dto.StudyCreateRequest;
import com.studiz.domain.study.dto.StudyDetailResponse;
import com.studiz.domain.study.dto.StudyResponse;
import com.studiz.domain.study.entity.Study;
import com.studiz.domain.study.service.StudyService;
import com.studiz.domain.studymember.service.StudyMemberService;
import com.studiz.domain.user.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/studies")
@Tag(name = "Study", description = "스터디 관리 API")
public class StudyController {

    private final StudyService studyService;
    private final StudyMemberService studyMemberService;

    @PostMapping
    @Operation(
            summary = "스터디 생성",
            description = "새로운 스터디를 생성합니다. 생성한 사용자는 자동으로 스터디장이 됩니다.\n\n" +
                    "**인증**: Bearer Token 필요\n\n" +
                    "**요청 필드**:\n" +
                    "- `name`: 스터디 이름 (필수, 2-50자)\n" +
                    "- `meetingName`: 모임명 (필수, 2-100자)\n" +
                    "- `maxMembers`: 최대 인원 (필수, 1명 이상)\n" +
                    "- `password`: 스터디 비밀번호 (필수, 4-50자)\n\n" +
                    "**요청 예시**:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"name\": \"Java 기초 스터디\",\n" +
                    "  \"meetingName\": \"주 2회 실시간 모임\",\n" +
                    "  \"maxMembers\": 10,\n" +
                    "  \"password\": \"study1234\"\n" +
                    "}\n" +
                    "```\n\n" +
                    "**응답 내용**:\n" +
                    "- 생성된 스터디 정보\n" +
                    "- `inviteCode`: 다른 사용자를 초대할 때 사용하는 8자리 코드 (자동 생성)\n\n" +
                    "**주의사항**:\n" +
                    "- 모든 필드는 필수입니다.\n" +
                    "- 생성자는 자동으로 스터디장(OWNER) 권한을 가집니다.\n" +
                    "- 초대코드는 자동으로 생성되며, 스터디 가입 시 사용됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스터디 생성 성공", content = @Content(schema = @Schema(implementation = StudyResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "인증 필요")
    })
    public ResponseEntity<StudyResponse> createStudy(
            @Valid @RequestBody StudyCreateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Study study = studyService.createStudy(
                request.getName(),
                request.getMeetingName(),
                request.getMaxMembers(),
                request.getPassword(),
                userPrincipal.getUser()
        );

        return ResponseEntity.ok(StudyResponse.from(study));
    }

    @GetMapping("/my-studies")
    @Operation(
            summary = "내가 가입한 스터디 목록 조회",
            description = "현재 로그인한 사용자가 가입한 모든 스터디 목록을 조회합니다.\n\n" +
                    "**인증**: Bearer Token 필요\n\n" +
                    "**응답 내용**:\n" +
                    "- 스터디 배열 (각 스터디마다 다음 정보 포함)\n" +
                    "  - `id`: 스터디 ID (UUID)\n" +
                    "  - `name`: 스터디 이름\n" +
                    "  - `inviteCode`: 초대코드 (8자리)\n" +
                    "  - `meetingName`: 모임명\n" +
                    "  - `maxMembers`: 최대 인원\n" +
                    "  - `status`: 스터디 상태 (ACTIVE, INACTIVE, COMPLETED)\n" +
                    "  - `createdAt`: 생성 일시\n\n" +
                    "**응답 예시**:\n" +
                    "```json\n" +
                    "[\n" +
                    "  {\n" +
                    "    \"id\": \"123e4567-e89b-12d3-a456-426614174000\",\n" +
                    "    \"name\": \"Java 기초 스터디\",\n" +
                    "    \"inviteCode\": \"abc12345\",\n" +
                    "    \"meetingName\": \"주 2회 실시간 모임\",\n" +
                    "    \"maxMembers\": 10,\n" +
                    "    \"status\": \"ACTIVE\",\n" +
                    "    \"createdAt\": \"2024-01-15T10:30:00\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"id\": \"456e7890-e89b-12d3-a456-426614174001\",\n" +
                    "    \"name\": \"Spring Boot 스터디\",\n" +
                    "    \"inviteCode\": \"def67890\",\n" +
                    "    \"meetingName\": \"주 3회 온라인 모임\",\n" +
                    "    \"maxMembers\": 8,\n" +
                    "    \"status\": \"ACTIVE\",\n" +
                    "    \"createdAt\": \"2024-01-16T14:20:00\"\n" +
                    "  }\n" +
                    "]\n" +
                    "```\n\n" +
                    "**사용 시나리오**:\n" +
                    "1. 전체 홈페이지 진입 시 이 API를 호출하여 사용자가 가입한 모든 스터디 ID 목록을 받습니다.\n" +
                    "2. 받은 스터디 ID들로 각 스터디의 투두를 조회합니다.\n" +
                    "   - 예: `GET /api/studies/{studyId}/todos`를 각 스터디 ID에 대해 호출\n" +
                    "3. 스터디 목록 화면에서 사용자가 가입한 스터디들을 표시합니다.\n\n" +
                    "**주의사항**:\n" +
                    "- 가입한 스터디가 없으면 빈 배열 `[]`이 반환됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = StudyResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 필요 (토큰이 없거나 유효하지 않음)")
    })
    public ResponseEntity<List<StudyResponse>> getMyStudies(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(studyService.getMyStudies(userPrincipal.getUser()));
    }

    @GetMapping("/invite/{code}")
    @Operation(
            summary = "초대코드로 스터디 조회",
            description = "초대코드를 사용하여 스터디 정보를 조회합니다. 인증 없이 접근 가능합니다.\n\n" +
                    "**사용 시나리오**:\n" +
                    "1. 사용자가 초대코드를 입력합니다.\n" +
                    "2. 이 API로 스터디 정보를 조회합니다.\n" +
                    "3. 스터디 정보를 확인한 후 가입 여부를 결정합니다.\n\n" +
                    "**요청 예시**:\n" +
                    "`GET /studies/invite/abc12345`\n\n" +
                    "**응답 내용**:\n" +
                    "- 스터디 기본 정보 (이름, 모임명, 최대 인원, 상태 등)\n" +
                    "- 초대코드로 스터디를 찾을 수 없으면 404 에러\n\n" +
                    "**주의사항**:\n" +
                    "- 초대코드는 8자리 문자열입니다.\n" +
                    "- 대소문자를 구분합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = StudyResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 초대코드")
    })
    public ResponseEntity<StudyResponse> getStudyByInviteCode(
            @Parameter(description = "초대코드 (8자리)", example = "abc12345", required = true)
            @PathVariable String code
    ) {
        Study study = studyService.findByInviteCode(code);
        return ResponseEntity.ok(StudyResponse.from(study));
    }
    
    @GetMapping("/{studyId}")
    @Operation(
            summary = "스터디 상세 조회",
            description = "스터디의 상세 정보와 멤버 목록을 조회합니다.\n\n" +
                    "**인증**: Bearer Token 필요\n\n" +
                    "**응답 내용**:\n" +
                    "- 스터디 기본 정보 (이름, 모임명, 최대 인원, 상태, 초대코드 등)\n" +
                    "- 멤버 목록 (각 멤버의 ID, 이름, 역할, 가입 일시 포함)\n\n" +
                    "**권한**: 스터디 멤버만 접근 가능\n\n" +
                    "**주의사항**:\n" +
                    "- 스터디에 가입하지 않은 사용자는 접근할 수 없습니다 (403 에러).\n" +
                    "- 존재하지 않는 스터디 ID인 경우 404 에러가 반환됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = StudyDetailResponse.class))),
            @ApiResponse(responseCode = "403", description = "스터디 멤버만 접근 가능"),
            @ApiResponse(responseCode = "404", description = "스터디를 찾을 수 없음")
    })
    public ResponseEntity<StudyDetailResponse> getStudyDetail(
            @Parameter(description = "스터디 ID", required = true)
            @PathVariable UUID studyId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(studyService.getStudyDetail(studyId, userPrincipal.getUser()));
    }
}
