package com.studiz.domain.todo.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class TodoAlreadyCompletedException extends CustomException {
    
    public TodoAlreadyCompletedException() {
        super(ErrorCode.TODO_ALREADY_COMPLETED);
    }
}

