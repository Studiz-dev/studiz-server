package com.studiz.domain.todo.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class TodoMemberForbiddenException extends CustomException {
    
    public TodoMemberForbiddenException() {
        super(ErrorCode.TODO_MEMBER_FORBIDDEN);
    }
}

