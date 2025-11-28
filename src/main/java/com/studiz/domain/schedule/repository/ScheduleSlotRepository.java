package com.studiz.domain.schedule.repository;

import com.studiz.domain.schedule.entity.Schedule;
import com.studiz.domain.schedule.entity.ScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, UUID> {

    List<ScheduleSlot> findBySchedule(Schedule schedule);

    List<ScheduleSlot> findByScheduleOrderByStartTimeAsc(Schedule schedule);
}

