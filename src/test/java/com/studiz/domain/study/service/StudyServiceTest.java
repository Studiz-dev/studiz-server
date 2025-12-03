package com.studiz.domain.study.service;

import com.studiz.domain.study.dto.StudyDetailResponse;
import com.studiz.domain.study.dto.StudyUpdateRequest;
import com.studiz.domain.study.entity.Study;
import com.studiz.domain.study.entity.StudyStatus;
import com.studiz.domain.studymember.entity.StudyMember;
import com.studiz.domain.studymember.entity.StudyMemberRole;
import com.studiz.domain.studymember.repository.StudyMemberRepository;
import com.studiz.domain.studymember.service.StudyMemberService;
import com.studiz.domain.user.entity.User;
import com.studiz.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class StudyServiceTest {
    
    @Autowired
    private StudyService studyService;
    
    @Autowired
    private StudyMemberService studyMemberService;
    
    @Autowired
    private StudyMemberRepository studyMemberRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private User owner;
    private User member;
    
    @BeforeEach
    void setUp() {
        owner = userRepository.save(User.builder()
                .loginId("owner1")
                .password("password")
                .name("Owner")
                .build());
        
        member = userRepository.save(User.builder()
                .loginId("member1")
                .password("password")
                .name("Member")
                .build());
    }
    
    @Test
    @DisplayName("스터디 상세 조회 시 멤버 목록을 포함해 반환한다")
    void getStudyDetail_withMembers() {
        Study study = studyService.createStudy("스터디", "모임", 10, "pass", owner);
        studyMemberService.joinStudy(study, member);
        
        StudyDetailResponse detail = studyService.getStudyDetail(study.getId(), owner);
        
        assertThat(detail.getMembers()).hasSize(2);
        assertThat(detail.getMembers())
                .anyMatch(m -> m.getLoginId().equals("owner1") && m.isOwner());
    }
    
    @Test
    @DisplayName("스터디장은 스터디 정보를 수정할 수 있다")
    void updateStudy_asOwner() {
        Study study = studyService.createStudy("스터디", "모임", 10, "pass", owner);
        
        StudyUpdateRequest request = new StudyUpdateRequest();
        request.setName("새 이름");
        request.setMeetingName("새 모임");
        request.setMaxMembers(20);
        request.setPassword("newpass");
        request.setStatus(StudyStatus.COMPLETED);
        
        Study updated = studyService.updateStudy(study.getId(), request, owner);
        
        assertThat(updated.getName()).isEqualTo("새 이름");
        assertThat(updated.getMeetingName()).isEqualTo("새 모임");
        assertThat(updated.getMaxMembers()).isEqualTo(20);
        assertThat(updated.getPassword()).isEqualTo("newpass");
        assertThat(updated.getStatus()).isEqualTo(StudyStatus.COMPLETED);
    }
    
    @Test
    @DisplayName("스터디장은 멤버를 강퇴할 수 있고 owner는 강퇴되지 않는다")
    void kickMember_byOwner() {
        Study study = studyService.createStudy("스터디", "모임", 10, "pass", owner);
        studyMemberService.joinStudy(study, member);
        StudyMember target = studyMemberRepository.findByStudyAndUser(study, member).orElseThrow();
        
        studyMemberService.kickMember(study, owner, target.getId());
        
        assertThat(studyMemberRepository.findByStudyAndUser(study, member)).isEmpty();
    }
    
    @Test
    @DisplayName("스터디장은 다른 멤버에게 owner 권한을 위임할 수 있다")
    void delegateOwnership() {
        Study study = studyService.createStudy("스터디", "모임", 10, "pass", owner);
        studyMemberService.joinStudy(study, member);
        StudyMember target = studyMemberRepository.findByStudyAndUser(study, member).orElseThrow();
        
        studyMemberService.delegateOwnership(study, owner, target.getId());
        
        StudyMember newOwner = studyMemberRepository.findByStudyAndUser(study, member).orElseThrow();
        StudyMember previousOwner = studyMemberRepository.findByStudyAndUser(study, owner).orElseThrow();
        
        assertThat(newOwner.getRole()).isEqualTo(StudyMemberRole.OWNER);
        assertThat(previousOwner.getRole()).isEqualTo(StudyMemberRole.MEMBER);
    }
}
