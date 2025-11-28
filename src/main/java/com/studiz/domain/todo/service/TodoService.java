package com.studiz.domain.todo.service;

import com.studiz.domain.notification.entity.NotificationType;
import com.studiz.domain.notification.service.NotificationService;
import com.studiz.domain.study.entity.Study;
import com.studiz.domain.study.service.StudyService;
import com.studiz.domain.studymember.repository.StudyMemberRepository;
import com.studiz.domain.studymember.service.StudyMemberService;
import com.studiz.domain.todo.dto.TodoCompleteRequest;
import com.studiz.domain.todo.dto.TodoCreateRequest;
import com.studiz.domain.todo.dto.TodoDetailResponse;
import com.studiz.domain.todo.dto.TodoResponse;
import com.studiz.domain.todo.entity.Todo;
import com.studiz.domain.todo.entity.TodoMember;
import com.studiz.domain.todo.exception.*;
import com.studiz.domain.todo.repository.TodoMemberRepository;
import com.studiz.domain.todo.repository.TodoRepository;
import com.studiz.domain.user.entity.User;
import com.studiz.domain.user.exception.UserNotFoundException;
import com.studiz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {
    
    private final TodoRepository todoRepository;
    private final TodoMemberRepository todoMemberRepository;
    private final StudyService studyService;
    private final StudyMemberService studyMemberService;
    private final StudyMemberRepository studyMemberRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    
    public TodoDetailResponse createTodo(UUID studyId, TodoCreateRequest request, User owner) {
        Study study = studyService.getStudy(studyId);
        studyMemberService.ensureOwner(study, owner);
        
        if (CollectionUtils.isEmpty(request.getParticipantIds())) {
            throw new TodoParticipantRequiredException();
        }
        
        List<User> participants = request.getParticipantIds().stream()
                .distinct()
                .map(this::getUser)
                .collect(Collectors.toList());
        
        participants.forEach(user -> studyMemberRepository.findByStudyAndUser(study, user)
                .orElseThrow(TodoInvalidParticipantException::new));
        
        Todo todo = Todo.create(
                study,
                request.getName(),
                request.getDescription(),
                request.getDueDate(),
                request.getCertificationType()
        );
        
        participants.forEach(user -> {
            TodoMember member = TodoMember.assign(todo, user);
            todo.addMember(member);
        });
        
        Todo saved = todoRepository.save(todo);
        
        // Todo 생성 알림을 참여자들에게 전송
        participants.forEach(participant -> {
            notificationService.createNotification(
                    participant,
                    NotificationType.TODO_CREATED,
                    "새로운 할 일이 생성되었습니다",
                    String.format("'%s' 스터디에 '%s' 할 일이 추가되었습니다.", study.getName(), saved.getName()),
                    saved.getId()
            );
        });
        
        return TodoDetailResponse.from(saved);
    }
    
    @Transactional(readOnly = true)
    public List<TodoResponse> getTodos(UUID studyId, User user) {
        Study study = studyService.getStudy(studyId);
        studyMemberService.ensureMember(study, user);
        return todoRepository.findByStudyOrderByDueDateAsc(study).stream()
                .map(TodoResponse::from)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public TodoDetailResponse getTodoDetail(UUID studyId, UUID todoId, User user) {
        Study study = studyService.getStudy(studyId);
        studyMemberService.ensureMember(study, user);
        Todo todo = getTodo(study, todoId);
        return TodoDetailResponse.from(todo);
    }
    
    public void completeTodo(UUID studyId, UUID todoId, User user, TodoCompleteRequest request) {
        Study study = studyService.getStudy(studyId);
        studyMemberService.ensureMember(study, user);
        Todo todo = getTodo(study, todoId);
        
        TodoMember todoMember = todoMemberRepository.findByTodoAndUser(todo, user)
                .orElseThrow(TodoMemberForbiddenException::new);
        
        if (todoMember.isCompleted()) {
            throw new TodoAlreadyCompletedException();
        }
        
        if (!StringUtils.hasText(request.getContent())) {
            throw new TodoCertificationRequiredException();
        }
        
        todoMember.complete(request.getContent());
        todoMemberRepository.save(todoMember);
        
        long remaining = todoMemberRepository.countByTodoAndCompletedFalse(todo);
        if (remaining == 0) {
            todo.markCompleted();
        }
    }
    
    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }
    
    private Todo getTodo(Study study, UUID todoId) {
        return todoRepository.findByIdAndStudy(todoId, study)
                .orElseThrow(TodoNotFoundException::new);
    }
}
