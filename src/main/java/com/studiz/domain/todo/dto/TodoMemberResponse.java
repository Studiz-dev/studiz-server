package com.studiz.domain.todo.dto;

import com.studiz.domain.todo.entity.TodoMember;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "Todo 참여자 정보")
public class TodoMemberResponse {
    
    @Schema(description = "멤버 ID (TodoMember ID)", example = "1")
    private final Long memberId;
    
    @Schema(description = "사용자 ID (User ID)", example = "1")
    private final Long userId;
    
    @Schema(description = "로그인 ID", example = "user123")
    private final String loginId;
    
    @Schema(description = "이름", example = "홍길동")
    private final String name;
    
    @Schema(description = "완료 여부", example = "true")
    private final boolean completed;
    
    @Schema(description = "완료 일시 (완료되지 않았으면 null)", example = "2024-01-18T15:30:00", nullable = true)
    private final LocalDateTime completedAt;
    
    @Schema(description = "텍스트 인증 내용 (완료되지 않았으면 null)", example = "문제를 모두 풀었습니다.", nullable = true)
    private final String certificationText;

    @Schema(description = "파일 인증 URL (완료되지 않았으면 null)", example = "https://example.com/proof.pdf", nullable = true)
    private final String certificationFileUrl;

    @Schema(description = "소감문 (완료되지 않았으면 null)", example = "과제가 유익했습니다.", nullable = true)
    private final String reflection;
    
    public static TodoMemberResponse from(TodoMember member) {
        return TodoMemberResponse.builder()
                .memberId(member.getId())
                .userId(member.getUser().getId())
                .loginId(member.getUser().getLoginId())
                .name(member.getUser().getName())
                .completed(member.isCompleted())
                .completedAt(member.getCompletedAt())
                .certificationText(member.getCertificationText())
                .certificationFileUrl(member.getCertificationFileUrl())
                .reflection(member.getReflection())
                .build();
    }
}
