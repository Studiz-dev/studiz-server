package com.studiz.domain.todo.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class TodoInvalidParticipantException extends CustomException {
    
    public TodoInvalidParticipantException() {
        super(ErrorCode.TODO_INVALID_PARTICIPANT);
    }
}

