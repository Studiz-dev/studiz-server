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
                    "**요청 예시**:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"name\": \"Java 기초 스터디\",\n" +
                    "  \"description\": \"Java 프로그래밍 기초를 함께 공부합니다.\"\n" +
                    "}\n" +
                    "```\n\n" +
                    "**응답 내용**:\n" +
                    "- 생성된 스터디 정보\n" +
                    "- `inviteCode`: 다른 사용자를 초대할 때 사용하는 8자리 코드\n\n" +
                    "**주의사항**:\n" +
                    "- 스터디 이름은 필수입니다.\n" +
                    "- 생성자는 자동으로 스터디장(OWNER) 권한을 가집니다."
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

    @GetMapping("/invite/{code}")
    @Operation(
            summary = "초대코드로 스터디 조회",
            description = "초대코드를 사용하여 스터디 정보를 조회합니다. 인증 없이 접근 가능합니다.\n\n" +
                    "**사용 시나리오**:\n" +
                    "1. 사용자가 초대코드를 입력합니다.\n" +
                    "2. 이 API로 스터디 정보를 조회합니다.\n" +
                    "3. 스터디 정보를 확인한 후 가입 여부를 결정합니다.\n\n" +
                    "**요청 예시**:\n" +
                    "`GET /studies/invite/abc12345`"
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
                    "**응답 내용**:\n" +
                    "- 스터디 기본 정보 (이름, 설명, 상태)\n" +
                    "- 멤버 목록 (각 멤버의 역할 포함)\n\n" +
                    "**권한**: 스터디 멤버만 접근 가능"
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
