package com.studiz.domain.user.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class UserNotFoundException extends CustomException {
    
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}

