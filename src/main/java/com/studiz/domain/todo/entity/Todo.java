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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    
    @Column(nullable = false)
    private LocalDateTime dueDate;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "todo_certification_types", joinColumns = @JoinColumn(name = "todo_id"))
    @Column(name = "certification_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<TodoCertificationType> certificationTypes = new HashSet<>();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TodoStatus status = TodoStatus.ACTIVE;
    
    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TodoMember> members = new ArrayList<>();
    
    public static Todo create(Study study,
                              String name,
                              LocalDateTime dueDate,
                              Set<TodoCertificationType> certificationTypes) {
        return Todo.builder()
                .study(study)
                .name(name)
                .dueDate(dueDate)
                .certificationTypes(certificationTypes)
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
