package com.studiz.domain.studymember.entity;

import com.studiz.domain.study.entity.Study;
import com.studiz.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "study_members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"study_id", "user_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StudyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyMemberRole role;

    @CreationTimestamp
    private LocalDateTime joinedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public static StudyMember create(Study study, User user, boolean owner) {
        return StudyMember.builder()
                .study(study)
                .user(user)
                .role(owner ? StudyMemberRole.OWNER : StudyMemberRole.MEMBER)
                .build();
    }

    public void promote() {
        this.role = StudyMemberRole.OWNER;
    }
    
    public void demoteToMember() {
        this.role = StudyMemberRole.MEMBER;
    }
}
