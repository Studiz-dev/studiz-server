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

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyStatus status = StudyStatus.ACTIVE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /** --- 생성 메서드 --- */
    public static Study create(String name, String description, User owner) {
        return Study.builder()
                .name(name)
                .description(description)
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
}
