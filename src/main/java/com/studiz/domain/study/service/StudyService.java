package com.studiz.domain.study.service;

import com.studiz.domain.study.entity.Study;
import com.studiz.domain.study.repository.StudyRepository;
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
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;

    public Study createStudy(String name, String description, User owner) {

        Study study = Study.create(name, description, owner);
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
                .orElseThrow(() -> new IllegalArgumentException("스터디를 찾을 수 없습니다."));
    }
}
