package com.studiz.domain.studymember.controller;

import com.studiz.domain.study.entity.Study;
import com.studiz.domain.study.service.StudyService;
import com.studiz.domain.studymember.service.StudyMemberService;
import com.studiz.domain.user.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/study-members")
@RequiredArgsConstructor
public class StudyMemberController {

    private final StudyMemberService studyMemberService;
    private final StudyService studyService;

    // ✅ 스터디 가입
    @PostMapping("/join/{inviteCode}")
    public ResponseEntity<String> joinStudy(
            @PathVariable String inviteCode,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Study study = studyService.findByInviteCode(inviteCode);
        studyMemberService.joinStudy(study, userPrincipal.getUser());
        return ResponseEntity.ok("스터디 가입 완료");
    }

    // ✅ 스터디 탈퇴
    @DeleteMapping("/leave/{inviteCode}")
    public ResponseEntity<String> leaveStudy(
            @PathVariable String inviteCode,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Study study = studyService.findByInviteCode(inviteCode);
        studyMemberService.leaveStudy(study, userPrincipal.getUser());
        return ResponseEntity.ok("스터디 탈퇴 완료");
    }
}
