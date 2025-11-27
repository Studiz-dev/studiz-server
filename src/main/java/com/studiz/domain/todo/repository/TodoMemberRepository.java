package com.studiz.domain.todo.repository;

import com.studiz.domain.todo.entity.Todo;
import com.studiz.domain.todo.entity.TodoMember;
import com.studiz.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoMemberRepository extends JpaRepository<TodoMember, Long> {
    
    Optional<TodoMember> findByTodoAndUser(Todo todo, User user);
    
    long countByTodoAndCompletedFalse(Todo todo);
    
    List<TodoMember> findByTodo(Todo todo);
}

