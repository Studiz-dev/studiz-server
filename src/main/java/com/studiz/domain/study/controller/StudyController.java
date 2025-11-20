package com.studiz.domain.study.controller;

import com.studiz.domain.study.dto.StudyCreateRequest;
import com.studiz.domain.study.dto.StudyResponse;
import com.studiz.domain.study.entity.Study;
import com.studiz.domain.study.service.StudyService;
import com.studiz.domain.user.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies")
public class StudyController {

    private final StudyService studyService;

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
}
