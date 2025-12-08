package com.studiz.domain.studymember.controller;

import com.studiz.domain.study.entity.Study;
import com.studiz.domain.study.service.StudyService;
import com.studiz.domain.studymember.service.StudyMemberService;
import com.studiz.domain.user.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/study-members")
@RequiredArgsConstructor
@Tag(name = "Study Member", description = "스터디 멤버 관리 API")
public class StudyMemberController {

    private final StudyMemberService studyMemberService;
    private final StudyService studyService;

    @PostMapping("/join/{inviteCode}")
    @Operation(
            summary = "스터디 가입",
            description = "초대코드를 사용하여 스터디에 가입합니다.\n\n" +
                    "**인증**: Bearer Token 필요\n\n" +
                    "**요청 예시**:\n" +
                    "`POST /study-members/join/abc12345`\n\n" +
                    "**동작**:\n" +
                    "1. 초대코드로 스터디를 조회합니다.\n" +
                    "2. 현재 사용자를 스터디 멤버로 추가합니다 (MEMBER 권한).\n" +
                    "3. 가입 알림이 전송됩니다.\n\n" +
                    "**응답**:\n" +
                    "- 성공 시: `\"스터디 가입 완료\"` 메시지 반환\n\n" +
                    "**주의사항**:\n" +
                    "- 이미 가입한 스터디에는 다시 가입할 수 없습니다 (409 에러).\n" +
                    "- 초대코드는 8자리 문자열이며, 대소문자를 구분합니다.\n" +
                    "- 존재하지 않는 초대코드인 경우 404 에러가 반환됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "가입 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 초대코드"),
            @ApiResponse(responseCode = "409", description = "이미 가입된 스터디")
    })
    public ResponseEntity<String> joinStudy(
            @Parameter(description = "초대코드 (8자리)", example = "abc12345", required = true)
            @PathVariable String inviteCode,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Study study = studyService.findByInviteCode(inviteCode);
        studyMemberService.joinStudy(study, userPrincipal.getUser());
        return ResponseEntity.ok("스터디 가입 완료");
    }
}
