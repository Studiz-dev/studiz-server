package com.studiz.domain.todo.entity;

import com.studiz.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "todo_members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"todo_id", "user_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TodoMember {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(columnDefinition = "TEXT")
    private String certificationText;

    @Column(length = 500)
    private String certificationFileUrl;

    @Column(length = 100)
    private String reflection;
    
    @Column(nullable = false)
    @Builder.Default
    private boolean completed = false;
    
    private LocalDateTime completedAt;
    
    public static TodoMember assign(Todo todo, User user) {
        return TodoMember.builder()
                .todo(todo)
                .user(user)
                .completed(false)
                .build();
    }
    
    public void complete(String textContent, String fileUrl, String reflection) {
        this.certificationText = textContent;
        this.certificationFileUrl = fileUrl;
        this.reflection = reflection;
        this.completed = true;
        this.completedAt = LocalDateTime.now();
    }
}
