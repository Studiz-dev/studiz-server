package com.studiz.domain.studymember.repository;

import com.studiz.domain.studymember.entity.StudyMember;
import com.studiz.domain.studymember.entity.StudyMemberRole;
import com.studiz.domain.study.entity.Study;
import com.studiz.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {

    boolean existsByStudyAndUser(Study study, User user);

    Optional<StudyMember> findByStudyAndUser(Study study, User user);

    List<StudyMember> findByStudy(Study study);

    List<StudyMember> findByUser(User user);
    
    Optional<StudyMember> findByIdAndStudy(Long id, Study study);
    
    long countByStudyAndRole(Study study, StudyMemberRole role);
}
