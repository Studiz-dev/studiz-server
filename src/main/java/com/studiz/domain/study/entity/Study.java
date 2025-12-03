package com.studiz.domain.study.entity;

import com.studiz.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "studies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

@Builder
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 10)
    private String inviteCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 100)
    private String meetingName;

    @Column(nullable = false)
    private Integer maxMembers;

    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StudyStatus status = StudyStatus.ACTIVE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /** --- 생성 메서드 --- */
    public static Study create(String name,
                               String meetingName,
                               Integer maxMembers,
                               String password,
                               User owner) {
        return Study.builder()
                .name(name)
                .meetingName(meetingName)
                .maxMembers(maxMembers)
                .password(password)
                .owner(owner)
                .inviteCode(UUID.randomUUID().toString().substring(0, 8))
                .status(StudyStatus.ACTIVE)
                .build();
    }

    public void close() {
        this.status = StudyStatus.INACTIVE;
    }

    public void complete() {
        this.status = StudyStatus.COMPLETED;
    }
    
    public void updateInfo(String name,
                           String meetingName,
                           Integer maxMembers,
                           StudyStatus status,
                           String password) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (meetingName != null && !meetingName.isBlank()) {
            this.meetingName = meetingName;
        }
        if (maxMembers != null && maxMembers > 0) {
            this.maxMembers = maxMembers;
        }
        if (status != null) {
            this.status = status;
        }
        if (password != null && !password.isBlank()) {
            this.password = password;
        }
    }
}
