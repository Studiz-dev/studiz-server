package com.studiz.domain.schedule.repository;

import com.studiz.domain.schedule.entity.Schedule;
import com.studiz.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

    List<Schedule> findByStudy(Study study);

    List<Schedule> findByStudyAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Study study, LocalDate endDate, LocalDate startDate
    );

    Optional<Schedule> findByIdAndStudy(UUID id, Study study);
}





