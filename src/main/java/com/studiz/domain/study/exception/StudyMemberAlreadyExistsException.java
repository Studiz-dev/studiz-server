package com.studiz.domain.study.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class StudyMemberAlreadyExistsException extends CustomException {
    
    public StudyMemberAlreadyExistsException() {
        super(ErrorCode.STUDY_MEMBER_ALREADY_EXISTS);
    }
}

