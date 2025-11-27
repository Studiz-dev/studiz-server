package com.studiz.domain.todo.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class TodoCertificationRequiredException extends CustomException {
    
    public TodoCertificationRequiredException() {
        super(ErrorCode.TODO_CERTIFICATION_REQUIRED);
    }
}

