package com.studiz.global.exception;

public class DuplicateLoginIdException extends CustomException {
    
    public DuplicateLoginIdException() {
        super(ErrorCode.DUPLICATE_LOGIN_ID);
    }
    
    public DuplicateLoginIdException(String message) {
        super(ErrorCode.DUPLICATE_LOGIN_ID, message);
    }
}

