package com.studiz.domain.todo.repository;

import com.studiz.domain.study.entity.Study;
import com.studiz.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoRepository extends JpaRepository<Todo, UUID> {
    
    List<Todo> findByStudyOrderByDueDateAsc(Study study);
    
    Optional<Todo> findByIdAndStudy(UUID id, Study study);
}

