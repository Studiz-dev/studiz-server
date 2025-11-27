package com.studiz.domain.todo.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class TodoParticipantRequiredException extends CustomException {
    
    public TodoParticipantRequiredException() {
        super(ErrorCode.TODO_PARTICIPANT_REQUIRED);
    }
}

