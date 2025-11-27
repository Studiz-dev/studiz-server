package com.studiz.domain.todo.service;

import com.studiz.domain.study.entity.Study;
import com.studiz.domain.study.service.StudyService;
import com.studiz.domain.studymember.service.StudyMemberService;
import com.studiz.domain.todo.dto.TodoCompleteRequest;
import com.studiz.domain.todo.dto.TodoCreateRequest;
import com.studiz.domain.todo.dto.TodoDetailResponse;
import com.studiz.domain.todo.dto.TodoResponse;
import com.studiz.domain.todo.entity.TodoCertificationType;
import com.studiz.domain.user.entity.User;
import com.studiz.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TodoServiceTest {
    
    @Autowired
    private TodoService todoService;
    
    @Autowired
    private StudyService studyService;
    
    @Autowired
    private StudyMemberService studyMemberService;
    
    @Autowired
    private UserRepository userRepository;
    
    private Study study;
    private User owner;
    private User participant;
    
    @BeforeEach
    void setUp() {
        owner = userRepository.save(User.builder()
                .loginId("owner")
                .password("password")
                .name("Owner")
                .build());
        
        participant = userRepository.save(User.builder()
                .loginId("member")
                .password("password")
                .name("Member")
                .build());
        
        study = studyService.createStudy("스터디", "설명", owner);
        studyMemberService.joinStudy(study, participant);
    }
    
    @Test
    @DisplayName("스터디장은 투두를 생성하고 조회할 수 있다")
    void createTodo() {
        TodoCreateRequest request = new TodoCreateRequest();
        request.setName("자료 조사");
        request.setDescription("다음 모임 전까지 조사");
        request.setDueDate(LocalDateTime.now().plusDays(2));
        request.setCertificationType(TodoCertificationType.TEXT_NOTE);
        request.setParticipantIds(List.of(participant.getId()));
        
        TodoDetailResponse response = todoService.createTodo(study.getId(), request, owner);
        assertThat(response.getTodo().getName()).isEqualTo("자료 조사");
        assertThat(response.getMembers()).hasSize(1);
        
        List<TodoResponse> todos = todoService.getTodos(study.getId(), owner);
        assertThat(todos).hasSize(1);
        assertThat(todos.get(0).getCompletedCount()).isZero();
    }
    
    @Test
    @DisplayName("참여자는 자신의 투두를 완료할 수 있다")
    void completeTodo() {
        TodoCreateRequest request = new TodoCreateRequest();
        request.setName("자료 조사");
        request.setDescription("다음 모임 전까지 조사");
        request.setDueDate(LocalDateTime.now().plusDays(2));
        request.setCertificationType(TodoCertificationType.TEXT_NOTE);
        request.setParticipantIds(List.of(participant.getId()));
        
        TodoDetailResponse detail = todoService.createTodo(study.getId(), request, owner);
        UUID todoId = detail.getTodo().getId();
        
        TodoCompleteRequest completeRequest = new TodoCompleteRequest();
        completeRequest.setContent("조사 완료");
        todoService.completeTodo(study.getId(), todoId, participant, completeRequest);
        
        TodoDetailResponse after = todoService.getTodoDetail(study.getId(), todoId, owner);
        assertThat(after.getTodo().getCompletedCount()).isEqualTo(1);
        assertThat(after.getTodo().getStatus().name()).isEqualTo("COMPLETED");
    }
}
