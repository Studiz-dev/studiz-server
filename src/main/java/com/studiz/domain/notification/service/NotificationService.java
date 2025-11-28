package com.studiz.domain.notification.service;

import com.studiz.domain.notification.dto.NotificationResponse;
import com.studiz.domain.notification.entity.Notification;
import com.studiz.domain.notification.entity.NotificationType;
import com.studiz.domain.notification.exception.NotificationNotFoundException;
import com.studiz.domain.notification.repository.NotificationRepository;
import com.studiz.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void createNotification(User user, NotificationType type, String title, String content, UUID relatedId) {
        Notification notification = Notification.create(user, type, title, content, relatedId);
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(User user) {
        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);
        return notifications.stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadNotifications(User user) {
        List<Notification> notifications = notificationRepository.findByUserAndReadFalseOrderByCreatedAtDesc(user);
        return notifications.stream()
                .map(NotificationResponse::from)
                .toList();
    }

    public void markAsRead(UUID notificationId, User user) {
        Notification notification = notificationRepository.findByIdAndUser(notificationId, user)
                .orElseThrow(NotificationNotFoundException::new);
        notification.markAsRead();
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(User user) {
        return notificationRepository.countByUserAndReadFalse(user);
    }
}

