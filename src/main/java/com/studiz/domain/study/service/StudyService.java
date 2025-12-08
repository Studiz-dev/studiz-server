package com.studiz.domain.study.service;

import com.studiz.domain.study.dto.StudyDetailResponse;
import com.studiz.domain.study.dto.StudyResponse;
import com.studiz.domain.study.dto.StudyUpdateRequest;
import com.studiz.domain.study.entity.Study;
import com.studiz.domain.study.exception.StudyNotFoundException;
import com.studiz.domain.study.repository.StudyRepository;
import com.studiz.domain.studymember.entity.StudyMember;
import com.studiz.domain.studymember.entity.StudyMemberRole;
import com.studiz.domain.studymember.repository.StudyMemberRepository;
import com.studiz.domain.studymember.service.StudyMemberService;
import com.studiz.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyMemberService studyMemberService;

    public Study createStudy(String name,
                             String meetingName,
                             Integer maxMembers,
                             String password,
                             User owner) {

        Study study = Study.create(name, meetingName, maxMembers, password, owner);
        Study saved = studyRepository.save(study);

        // 스터디장 자동 등록
        StudyMember ownerMember = StudyMember.builder()
                .study(saved)
                .user(owner)
                .role(StudyMemberRole.OWNER)
                .build();

        studyMemberRepository.save(ownerMember);

        return saved;
    }

    public Study findByInviteCode(String inviteCode) {
        return studyRepository.findByInviteCode(inviteCode)
                .orElseThrow(StudyNotFoundException::new);
    }
    
    public Study getStudy(UUID studyId) {
        return studyRepository.findById(studyId)
                .orElseThrow(StudyNotFoundException::new);
    }
    
    @Transactional
    public Study updateStudy(UUID studyId, StudyUpdateRequest request, User user) {
        Study study = getStudy(studyId);
        studyMemberService.ensureOwner(study, user);
        study.updateInfo(
                request.getName(),
                request.getMeetingName(),
                request.getMaxMembers(),
                request.getStatus(),
                request.getPassword()
        );
        return study;
    }
    
    @Transactional
    public StudyDetailResponse getStudyDetail(UUID studyId, User user) {
        Study study = getStudy(studyId);
        studyMemberService.ensureMember(study, user);
        return StudyDetailResponse.from(study, studyMemberService.getMembers(study));
    }
    
    @Transactional(readOnly = true)
    public List<StudyResponse> getMyStudies(User user) {
        List<StudyMember> members = studyMemberRepository.findByUser(user);
        return members.stream()
                .map(StudyMember::getStudy)
                .map(StudyResponse::from)
                .collect(Collectors.toList());
    }
}
