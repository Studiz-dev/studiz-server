package com.studiz.domain.todo.dto;

import com.studiz.domain.todo.entity.TodoCertificationType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TodoCreateRequest {
    
    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 100, message = "이름은 100자 이하여야 합니다.")
    private String name;
    
    @Size(max = 1000, message = "설명은 1000자 이하여야 합니다.")
    private String description;
    
    @NotNull(message = "마감일은 필수입니다.")
    @Future(message = "마감일은 현재 이후여야 합니다.")
    private LocalDateTime dueDate;
    
    @NotNull(message = "인증 방식은 필수입니다.")
    private TodoCertificationType certificationType;
    
    @NotEmpty(message = "참여자는 최소 1명 이상이어야 합니다.")
    private List<Long> participantIds;
}
