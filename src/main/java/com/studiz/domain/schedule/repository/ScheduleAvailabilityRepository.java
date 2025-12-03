package com.studiz.domain.schedule.repository;

import com.studiz.domain.schedule.entity.ScheduleAvailability;
import com.studiz.domain.schedule.entity.ScheduleSlot;
import com.studiz.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleAvailabilityRepository extends JpaRepository<ScheduleAvailability, Long> {

    Optional<ScheduleAvailability> findBySlotAndUser(ScheduleSlot slot, User user);

    List<ScheduleAvailability> findBySlot(ScheduleSlot slot);

    List<ScheduleAvailability> findBySlotIn(List<ScheduleSlot> slots);

    @Query("SELECT sa.slot.id, COUNT(sa) " +
           "FROM ScheduleAvailability sa " +
           "WHERE sa.slot.schedule.id = :scheduleId AND sa.available = true " +
           "GROUP BY sa.slot.id")
    List<Object[]> countAvailableUsersBySlot(@Param("scheduleId") UUID scheduleId);

    void deleteBySlotIn(List<ScheduleSlot> slots);
}






