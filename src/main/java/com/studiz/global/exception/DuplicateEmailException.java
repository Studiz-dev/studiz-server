package com.studiz.global.exception;

public class DuplicateEmailException extends CustomException {
    
    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_LOGIN_ID);
    }
    
    public DuplicateEmailException(String message) {
        super(ErrorCode.DUPLICATE_LOGIN_ID, message);
    }
}

