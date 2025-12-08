package com.studiz.domain.schedule.entity;

import com.studiz.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "schedule_availability",
        uniqueConstraints = @UniqueConstraint(columnNames = {"slot_id", "user_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ScheduleAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    private ScheduleSlot slot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Builder.Default
    private boolean available = true;

    public static ScheduleAvailability create(ScheduleSlot slot, User user, boolean available) {
        return ScheduleAvailability.builder()
                .slot(slot)
                .user(user)
                .available(available)
                .build();
    }

    public void updateAvailability(boolean available) {
        this.available = available;
    }
}










