package com.studiz.domain.todo.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class TodoNotFoundException extends CustomException {
    
    public TodoNotFoundException() {
        super(ErrorCode.TODO_NOT_FOUND);
    }
}

