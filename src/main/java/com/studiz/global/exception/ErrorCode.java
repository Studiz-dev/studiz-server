package com.studiz.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    
    // User & Auth
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "유효하지 않은 입력값입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    
    // Study & StudyMember
    STUDY_NOT_FOUND(HttpStatus.NOT_FOUND, "스터디를 찾을 수 없습니다."),
    STUDY_MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 가입된 스터디입니다."),
    STUDY_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "스터디 멤버를 찾을 수 없습니다."),
    STUDY_MEMBER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "스터디 멤버만 접근할 수 있습니다."),
    STUDY_OWNER_REQUIRED(HttpStatus.FORBIDDEN, "스터디장만 수행할 수 있는 작업입니다."),
    STUDY_OWNER_CANNOT_LEAVE(HttpStatus.BAD_REQUEST, "스터디장은 탈퇴 전에 권한을 위임해야 합니다."),
    STUDY_OWNER_CANNOT_BE_REMOVED(HttpStatus.BAD_REQUEST, "스터디장은 강퇴할 수 없습니다."),
    STUDY_MEMBER_ALREADY_OWNER(HttpStatus.BAD_REQUEST, "이미 스터디장 권한을 가진 멤버입니다."),
    
    // Todo
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "할 일을 찾을 수 없습니다."),
    TODO_PARTICIPANT_REQUIRED(HttpStatus.BAD_REQUEST, "할 일 참여자는 최소 한 명 이상이어야 합니다."),
    TODO_INVALID_PARTICIPANT(HttpStatus.BAD_REQUEST, "스터디 멤버만 할 일에 참여할 수 있습니다."),
    TODO_MEMBER_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 할 일의 참가자가 아닙니다."),
    TODO_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "이미 완료된 할 일입니다."),
    TODO_CERTIFICATION_REQUIRED(HttpStatus.BAD_REQUEST, "인증 내용이 필요합니다."),
    
    // Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");
    
    private final HttpStatus status;
    private final String message;
}
