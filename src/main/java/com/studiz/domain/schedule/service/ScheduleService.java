package com.studiz.domain.schedule.service;

import com.studiz.domain.notification.entity.NotificationType;
import com.studiz.domain.notification.service.NotificationService;
import com.studiz.domain.schedule.dto.ScheduleCreateRequest;
import com.studiz.domain.schedule.dto.ScheduleDetailResponse;
import com.studiz.domain.schedule.dto.ScheduleResponse;
import com.studiz.domain.schedule.entity.Schedule;
import com.studiz.domain.schedule.entity.ScheduleSlot;
import com.studiz.domain.schedule.exception.ScheduleInvalidDateRangeException;
import com.studiz.domain.schedule.exception.ScheduleNotFoundException;
import com.studiz.domain.schedule.repository.ScheduleRepository;
import com.studiz.domain.schedule.repository.ScheduleSlotRepository;
import com.studiz.domain.study.entity.Study;
import com.studiz.domain.study.service.StudyService;
import com.studiz.domain.studymember.entity.StudyMember;
import com.studiz.domain.studymember.service.StudyMemberService;
import com.studiz.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleSlotRepository scheduleSlotRepository;
    private final StudyService studyService;
    private final StudyMemberService studyMemberService;
    private final NotificationService notificationService;

    public ScheduleResponse createSchedule(UUID studyId, ScheduleCreateRequest request, User user) {
        Study study = studyService.getStudy(studyId);
        studyMemberService.ensureOwner(study, user);

        // endDate는 startDate와 동일하게 설정 (팀장이 시간 확정하면 끝)
        LocalDate scheduleDate = request.getStartDate();
        Schedule schedule = Schedule.create(
                study,
                request.getTitle(),
                scheduleDate,
                scheduleDate
        );

        Schedule saved = scheduleRepository.save(schedule);

        // 해당 날짜의 1시간 단위 시간 슬롯 생성 (00:00 ~ 23:00)
        List<ScheduleSlot> slots = generateTimeSlots(saved, scheduleDate, scheduleDate);
        scheduleSlotRepository.saveAll(slots);

        return ScheduleResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedules(UUID studyId, String month, User user) {
        Study study = studyService.getStudy(studyId);
        studyMemberService.ensureMember(study, user);

        // 필터링 기능 제거 - 모든 일정 반환
        List<Schedule> schedules = scheduleRepository.findByStudy(study);

        // 확정된 일정 우선, 그 다음 날짜 오름차순 정렬
        return schedules.stream()
                .sorted((s1, s2) -> {
                    boolean s1Confirmed = s1.getConfirmedSlot() != null;
                    boolean s2Confirmed = s2.getConfirmedSlot() != null;
                    
                    if (s1Confirmed != s2Confirmed) {
                        return s1Confirmed ? -1 : 1; // 확정된 일정이 먼저
                    }
                    
                    // 둘 다 확정되었거나 둘 다 미확정인 경우 날짜 비교
                    LocalDate date1 = s1.getConfirmedSlot() != null 
                            ? s1.getConfirmedSlot().getStartTime().toLocalDate() 
                            : s1.getStartDate();
                    LocalDate date2 = s2.getConfirmedSlot() != null 
                            ? s2.getConfirmedSlot().getStartTime().toLocalDate() 
                            : s2.getStartDate();
                    
                    return date1.compareTo(date2);
                })
                .map(ScheduleResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ScheduleDetailResponse getScheduleDetail(UUID studyId, UUID scheduleId, User user) {
        Study study = studyService.getStudy(studyId);
        studyMemberService.ensureMember(study, user);

        Schedule schedule = scheduleRepository.findByIdAndStudy(scheduleId, study)
                .orElseThrow(ScheduleNotFoundException::new);

        List<ScheduleSlot> slots = scheduleSlotRepository.findByScheduleOrderByStartTimeAsc(schedule);

        return ScheduleDetailResponse.from(schedule, slots);
    }

    public void confirmSchedule(UUID studyId, UUID scheduleId, UUID slotId, User user) {
        Study study = studyService.getStudy(studyId);
        studyMemberService.ensureOwner(study, user);

        Schedule schedule = scheduleRepository.findByIdAndStudy(scheduleId, study)
                .orElseThrow(ScheduleNotFoundException::new);

        ScheduleSlot slot = scheduleSlotRepository.findById(slotId)
                .orElseThrow(() -> new ScheduleNotFoundException());

        if (!slot.getSchedule().getId().equals(schedule.getId())) {
            throw new ScheduleNotFoundException();
        }

        schedule.confirmSlot(slot);

        // 스케줄 확정 알림을 스터디 멤버들에게 전송
        List<StudyMember> members = studyMemberService.getMembers(study);
        members.forEach(member -> {
            notificationService.createNotification(
                    member.getUser(),
                    NotificationType.SCHEDULE_CONFIRMED,
                    "일정이 확정되었습니다",
                    String.format("'%s' 스터디의 '%s' 일정이 확정되었습니다.", study.getName(), schedule.getTitle()),
                    schedule.getId()
            );
        });
    }

    private List<ScheduleSlot> generateTimeSlots(Schedule schedule, LocalDate startDate, LocalDate endDate) {
        List<ScheduleSlot> slots = new java.util.ArrayList<>();
        LocalDate currentDate = startDate;
        LocalTime startTime = LocalTime.of(0, 0);
        LocalTime endTime = LocalTime.of(23, 0);

        while (!currentDate.isAfter(endDate)) {
            LocalDateTime slotStart = LocalDateTime.of(currentDate, startTime);
            LocalDateTime dayEnd = LocalDateTime.of(currentDate, endTime);

            while (!slotStart.isAfter(dayEnd)) {
                slots.add(ScheduleSlot.create(schedule, slotStart));
                slotStart = slotStart.plusHours(1);
            }

            currentDate = currentDate.plusDays(1);
        }

        return slots;
    }
}

