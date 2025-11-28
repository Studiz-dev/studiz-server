package com.studiz.domain.calendar.service;

import com.studiz.domain.calendar.dto.CalendarDayResponse;
import com.studiz.domain.calendar.dto.CalendarMonthResponse;
import com.studiz.domain.study.entity.Study;
import com.studiz.domain.study.service.StudyService;
import com.studiz.domain.studymember.service.StudyMemberService;
import com.studiz.domain.todo.dto.TodoResponse;
import com.studiz.domain.todo.entity.Todo;
import com.studiz.domain.todo.entity.TodoMember;
import com.studiz.domain.todo.repository.TodoMemberRepository;
import com.studiz.domain.todo.repository.TodoRepository;
import com.studiz.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

    private final TodoRepository todoRepository;
    private final TodoMemberRepository todoMemberRepository;
    private final StudyService studyService;
    private final StudyMemberService studyMemberService;

    public CalendarMonthResponse getMonthSummary(UUID studyId, int year, int month, User user) {
        Study study = studyService.getStudy(studyId);
        studyMemberService.ensureMember(study, user);

        List<Todo> todos = todoRepository.findByStudyAndYearAndMonth(study, year, month);

        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
        LocalDate today = LocalDate.now();

        List<CalendarMonthResponse.DaySummary> daySummaries = new ArrayList<>();

        for (LocalDate date = firstDay; !date.isAfter(lastDay); date = date.plusDays(1)) {
            LocalDate finalDate = date;
            List<Todo> dayTodos = todos.stream()
                    .filter(todo -> todo.getDueDate().toLocalDate().equals(finalDate))
                    .toList();

            double completionRate = calculateCompletionRate(dayTodos);
            long dDay = calculateDDay(date, today);
            int todoCount = dayTodos.size();

            daySummaries.add(CalendarMonthResponse.DaySummary.builder()
                    .date(date)
                    .completionRate(completionRate)
                    .dDay(dDay)
                    .todoCount(todoCount)
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

        List<Todo> todos = todoRepository.findByStudyAndDueDate(study, date);

        List<TodoResponse> todoResponses = todos.stream()
                .map(TodoResponse::from)
                .toList();

        double overallCompletionRate = calculateCompletionRate(todos);
        long dDay = calculateDDay(date, LocalDate.now());

        return CalendarDayResponse.builder()
                .date(date)
                .todos(todoResponses)
                .overallCompletionRate(overallCompletionRate)
                .dDay(dDay)
                .build();
    }

    private double calculateCompletionRate(List<Todo> todos) {
        if (todos.isEmpty()) {
            return 0.0;
        }

        int totalMembers = 0;
        int completedMembers = 0;

        for (Todo todo : todos) {
            List<TodoMember> members = todoMemberRepository.findByTodo(todo);
            totalMembers += members.size();
            completedMembers += (int) members.stream()
                    .filter(TodoMember::isCompleted)
                    .count();
        }

        if (totalMembers == 0) {
            return 0.0;
        }

        return (double) completedMembers / totalMembers * 100.0;
    }

    private long calculateDDay(LocalDate dueDate, LocalDate today) {
        return ChronoUnit.DAYS.between(today, dueDate);
    }
}

