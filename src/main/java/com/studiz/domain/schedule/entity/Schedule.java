package com.studiz.domain.schedule.entity;

import com.studiz.domain.study.entity.Study;
import com.studiz.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmed_slot_id")
    private ScheduleSlot confirmedSlot;

    public static Schedule create(Study study, String title, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작일은 종료일보다 이전이어야 합니다.");
        }
        
        return Schedule.builder()
                .study(study)
                .title(title)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public void confirmSlot(ScheduleSlot slot) {
        if (!slot.getSchedule().getId().equals(this.id)) {
            throw new IllegalArgumentException("해당 스케줄의 슬롯이 아닙니다.");
        }
        this.confirmedSlot = slot;
    }
}

