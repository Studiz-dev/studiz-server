package com.studiz.domain.study.dto;

import com.studiz.domain.study.entity.StudyStatus;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyUpdateRequest {
    
    @Size(min = 2, max = 50, message = "스터디 이름은 2~50자 사이여야 합니다.")
    private String name;
    
    @Size(max = 500, message = "설명은 500자 이하여야 합니다.")
    private String description;
    
    private StudyStatus status;
}
