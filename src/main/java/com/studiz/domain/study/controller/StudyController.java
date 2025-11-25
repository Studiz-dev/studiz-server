package com.studiz.domain.study.controller;

import com.studiz.domain.study.dto.StudyCreateRequest;
import com.studiz.domain.study.dto.StudyDetailResponse;
import com.studiz.domain.study.dto.StudyResponse;
import com.studiz.domain.study.dto.StudyUpdateRequest;
import com.studiz.domain.study.entity.Study;
import com.studiz.domain.study.service.StudyService;
import com.studiz.domain.studymember.service.StudyMemberService;
import com.studiz.domain.user.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies")
public class StudyController {

    private final StudyService studyService;
    private final StudyMemberService studyMemberService;

    /** 스터디 생성 */
    @PostMapping
    public ResponseEntity<StudyResponse> createStudy(
            @RequestBody StudyCreateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Study study = studyService.createStudy(
                request.getName(),
                request.getDescription(),
                userPrincipal.getUser()
        );

        return ResponseEntity.ok(StudyResponse.from(study));
    }

    /** 초대코드로 스터디 조회 */
    @GetMapping("/invite/{code}")
    public ResponseEntity<StudyResponse> getStudyByInviteCode(
            @PathVariable String code
    ) {
        Study study = studyService.findByInviteCode(code);
        return ResponseEntity.ok(StudyResponse.from(study));
    }
    
    /** 스터디 상세 (멤버 포함) */
    @GetMapping("/{studyId}")
    public ResponseEntity<StudyDetailResponse> getStudyDetail(
            @PathVariable UUID studyId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(studyService.getStudyDetail(studyId, userPrincipal.getUser()));
    }
    
    /** 스터디 정보 수정 (owner 전용) */
    @PutMapping("/{studyId}")
    public ResponseEntity<StudyResponse> updateStudy(
            @PathVariable UUID studyId,
            @Valid @RequestBody StudyUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Study updated = studyService.updateStudy(studyId, request, userPrincipal.getUser());
        return ResponseEntity.ok(StudyResponse.from(updated));
    }
    
    /** 스터디 멤버 강퇴 (owner 전용) */
    @DeleteMapping("/{studyId}/members/{memberId}")
    public ResponseEntity<String> kickStudyMember(
            @PathVariable UUID studyId,
            @PathVariable Long memberId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Study study = studyService.getStudy(studyId);
        studyMemberService.kickMember(study, userPrincipal.getUser(), memberId);
        return ResponseEntity.ok("스터디 강퇴 완료");
    }
    
    /** owner 위임 */
    @PostMapping("/{studyId}/members/{memberId}/delegate")
    public ResponseEntity<String> delegateStudyOwner(
            @PathVariable UUID studyId,
            @PathVariable Long memberId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Study study = studyService.getStudy(studyId);
        studyMemberService.delegateOwnership(study, userPrincipal.getUser(), memberId);
        return ResponseEntity.ok("스터디장 위임 완료");
    }
}
