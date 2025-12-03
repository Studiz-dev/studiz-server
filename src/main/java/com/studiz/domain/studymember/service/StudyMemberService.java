package com.studiz.domain.studymember.service;

import com.studiz.domain.study.entity.Study;
import com.studiz.domain.study.exception.StudyMemberAccessDeniedException;
import com.studiz.domain.study.exception.StudyMemberAlreadyExistsException;
import com.studiz.domain.study.exception.StudyMemberAlreadyOwnerException;
import com.studiz.domain.study.exception.StudyMemberNotFoundException;
import com.studiz.domain.study.exception.StudyOwnerCannotBeRemovedException;
import com.studiz.domain.study.exception.StudyOwnerCannotLeaveException;
import com.studiz.domain.study.exception.StudyOwnerOnlyOperationException;
import com.studiz.domain.studymember.entity.StudyMember;
import com.studiz.domain.studymember.entity.StudyMemberRole;
import com.studiz.domain.studymember.repository.StudyMemberRepository;
import com.studiz.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyMemberService {

    private final StudyMemberRepository studyMemberRepository;

    public void joinStudy(Study study, User user) {
        if (studyMemberRepository.existsByStudyAndUser(study, user)) {
            throw new StudyMemberAlreadyExistsException();
        }

        StudyMember member = StudyMember.builder()
                .study(study)
                .user(user)
                .role(StudyMemberRole.MEMBER)
                .build();

        studyMemberRepository.save(member);
    }

    public void leaveStudy(Study study, User user) {
        StudyMember member = studyMemberRepository.findByStudyAndUser(study, user)
                .orElseThrow(StudyMemberNotFoundException::new);

        if (member.getRole() == StudyMemberRole.OWNER) {
            long ownerCount = studyMemberRepository.countByStudyAndRole(study, StudyMemberRole.OWNER);
            if (ownerCount <= 1) {
                throw new StudyOwnerCannotLeaveException();
            }
        }

        studyMemberRepository.delete(member);
    }
    
    public void ensureMember(Study study, User user) {
        studyMemberRepository.findByStudyAndUser(study, user)
                .orElseThrow(StudyMemberAccessDeniedException::new);
    }
    
    public StudyMember ensureOwner(Study study, User user) {
        StudyMember member = studyMemberRepository.findByStudyAndUser(study, user)
                .orElseThrow(StudyMemberAccessDeniedException::new);
        if (member.getRole() != StudyMemberRole.OWNER) {
            throw new StudyOwnerOnlyOperationException();
        }
        return member;
    }
    
    public List<StudyMember> getMembers(Study study) {
        return studyMemberRepository.findByStudy(study);
    }
    
    public void kickMember(Study study, User owner, Long memberId) {
        ensureOwner(study, owner);
        StudyMember target = studyMemberRepository.findByIdAndStudy(memberId, study)
                .orElseThrow(StudyMemberNotFoundException::new);
        if (target.getRole() == StudyMemberRole.OWNER) {
            throw new StudyOwnerCannotBeRemovedException();
        }
        studyMemberRepository.delete(target);
    }
    
    public void delegateOwnership(Study study, User owner, Long memberId) {
        StudyMember currentOwner = ensureOwner(study, owner);
        StudyMember target = studyMemberRepository.findByIdAndStudy(memberId, study)
                .orElseThrow(StudyMemberNotFoundException::new);
        if (target.getRole() == StudyMemberRole.OWNER) {
            throw new StudyMemberAlreadyOwnerException();
        }
        currentOwner.demoteToMember();
        target.promote();
    }
}
