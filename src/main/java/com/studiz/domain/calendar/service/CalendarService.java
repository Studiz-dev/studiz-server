package com.studiz.domain.calendar.service;

import com.studiz.domain.calendar.dto.CalendarDayResponse;
import com.studiz.domain.calendar.dto.CalendarMonthResponse;
import com.studiz.domain.schedule.entity.Schedule;
import com.studiz.domain.schedule.repository.ScheduleRepository;
import com.studiz.domain.study.entity.Study;
import com.studiz.domain.study.service.StudyService;
import com.studiz.domain.studymember.service.StudyMemberService;
import com.studiz.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

    private final ScheduleRepository scheduleRepository;
    private final StudyService studyService;
    private final StudyMemberService studyMemberService;

    public CalendarMonthResponse getMonthSummary(UUID studyId, int year, int month, User user) {
        Study study = studyService.getStudy(studyId);
        studyMemberService.ensureMember(study, user);

        // 해당 월에 확정된 일정만 조회
        List<Schedule> schedules = scheduleRepository.findByStudy(study);
        List<Schedule> confirmedSchedules = schedules.stream()
                .filter(schedule -> schedule.getConfirmedSlot() != null)
                .filter(schedule -> {
                    LocalDate scheduleDate = schedule.getConfirmedSlot().getStartTime().toLocalDate();
                    return scheduleDate.getYear() == year && scheduleDate.getMonthValue() == month;
                })
                .collect(Collectors.toList());

        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        List<CalendarMonthResponse.DaySummary> daySummaries = new ArrayList<>();

        for (LocalDate date = firstDay; !date.isAfter(lastDay); date = date.plusDays(1)) {
            LocalDate finalDate = date;
            List<String> scheduleTitles = confirmedSchedules.stream()
                    .filter(schedule -> schedule.getConfirmedSlot().getStartTime().toLocalDate().equals(finalDate))
                    .sorted((s1, s2) -> s1.getConfirmedSlot().getStartTime().compareTo(s2.getConfirmedSlot().getStartTime()))
                    .map(Schedule::getTitle)
                    .limit(3)
                    .collect(Collectors.toList());

            daySummaries.add(CalendarMonthResponse.DaySummary.builder()
                    .date(date)
                    .scheduleTitles(scheduleTitles)
                    .build());
        }

        return CalendarMonthResponse.builder()
                .year(year)
                .month(month)
                .days(daySummaries)
                .build();
    }

    public CalendarDayResponse getDayDetail(UUID studyId, LocalDate date, User user) {
        Study study = studyService.getStudy(studyId);
        studyMemberService.ensureMember(study, user);

        // 해당 날짜에 확정된 일정 조회
        List<Schedule> schedules = scheduleRepository.findByStudy(study);
        List<CalendarDayResponse.ScheduleInfo> scheduleInfos = schedules.stream()
                .filter(schedule -> schedule.getConfirmedSlot() != null)
                .filter(schedule -> schedule.getConfirmedSlot().getStartTime().toLocalDate().equals(date))
                .sorted((s1, s2) -> s1.getConfirmedSlot().getStartTime().compareTo(s2.getConfirmedSlot().getStartTime()))
                .map(schedule -> {
                    LocalDate today = LocalDate.now();
                    long dDay = ChronoUnit.DAYS.between(today, date);
                    LocalDateTime scheduleDateTime = schedule.getConfirmedSlot().getStartTime();
                    String scheduleTime = scheduleDateTime.format(DateTimeFormatter.ofPattern("yyyy M d HH:mm"));
                    
                    return CalendarDayResponse.ScheduleInfo.builder()
                            .id(schedule.getId())
                            .title(schedule.getTitle())
                            .location(schedule.getLocation())
                            .scheduleTime(scheduleTime)
                            .dDay(dDay)
                            .build();
                })
                .collect(Collectors.toList());

        return CalendarDayResponse.builder()
                .date(date)
                .schedules(scheduleInfos)
                .build();
    }
}


