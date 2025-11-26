package com.studiz.domain.todo.entity;

import com.studiz.domain.study.entity.Study;
import com.studiz.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "todos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Todo extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private LocalDateTime dueDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoCertificationType certificationType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TodoStatus status = TodoStatus.ACTIVE;
    
    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TodoMember> members = new ArrayList<>();
    
    public static Todo create(Study study,
                              String name,
                              String description,
                              LocalDateTime dueDate,
                              TodoCertificationType certificationType) {
        return Todo.builder()
                .study(study)
                .name(name)
                .description(description)
                .dueDate(dueDate)
                .certificationType(certificationType)
                .status(TodoStatus.ACTIVE)
                .build();
    }
    
    public void addMember(TodoMember member) {
        this.members.add(member);
    }
    
    public void markCompleted() {
        this.status = TodoStatus.COMPLETED;
    }
}
