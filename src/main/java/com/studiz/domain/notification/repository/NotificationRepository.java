package com.studiz.domain.notification.repository;

import com.studiz.domain.notification.entity.Notification;
import com.studiz.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    List<Notification> findByUserAndReadFalseOrderByCreatedAtDesc(User user);

    Optional<Notification> findByIdAndUser(UUID id, User user);

    long countByUserAndReadFalse(User user);
}






