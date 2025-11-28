package com.studiz.domain.todo.repository;

import com.studiz.domain.study.entity.Study;
import com.studiz.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoRepository extends JpaRepository<Todo, UUID> {
    
    List<Todo> findByStudyOrderByDueDateAsc(Study study);
    
    Optional<Todo> findByIdAndStudy(UUID id, Study study);

    @Query("SELECT t FROM Todo t WHERE t.study = :study AND DATE(t.dueDate) = :date")
    List<Todo> findByStudyAndDueDate(@Param("study") Study study, @Param("date") LocalDate date);

    @Query("SELECT t FROM Todo t WHERE t.study = :study AND YEAR(t.dueDate) = :year AND MONTH(t.dueDate) = :month")
    List<Todo> findByStudyAndYearAndMonth(@Param("study") Study study, @Param("year") int year, @Param("month") int month);
}

