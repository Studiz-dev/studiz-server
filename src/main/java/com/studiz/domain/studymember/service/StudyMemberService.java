package com.studiz.domain.studymember.service;

import com.studiz.domain.study.entity.Study;
import com.studiz.domain.studymember.entity.StudyMember;
import com.studiz.domain.studymember.entity.StudyMemberRole;
import com.studiz.domain.studymember.repository.StudyMemberRepository;
import com.studiz.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyMemberService {

    private final StudyMemberRepository studyMemberRepository;

    public void joinStudy(Study study, User user) {
        if (studyMemberRepository.existsByStudyAndUser(study, user)) {
            throw new IllegalStateException("이미 가입된 스터디입니다.");
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
                .orElseThrow(() -> new IllegalArgumentException("가입된 스터디 멤버가 아닙니다."));

        studyMemberRepository.delete(member);
    }
}
