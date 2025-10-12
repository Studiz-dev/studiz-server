package com.studiz.global.exception;

public class DuplicateEmailException extends CustomException {
    
    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }
    
    public DuplicateEmailException(String message) {
        super(ErrorCode.DUPLICATE_EMAIL, message);
    }
}

