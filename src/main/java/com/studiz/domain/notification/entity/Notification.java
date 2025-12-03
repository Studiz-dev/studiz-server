package com.studiz.domain.notification.entity;

import com.studiz.domain.user.entity.User;
import com.studiz.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Builder.Default
    private boolean read = false;

    @Column
    private UUID relatedId; // 관련 엔티티 ID (studyId, todoId, scheduleId 등)

    public static Notification create(User user, NotificationType type, String title, String content, UUID relatedId) {
        return Notification.builder()
                .user(user)
                .type(type)
                .title(title)
                .content(content)
                .relatedId(relatedId)
                .read(false)
                .build();
    }

    public void markAsRead() {
        this.read = true;
    }
}





