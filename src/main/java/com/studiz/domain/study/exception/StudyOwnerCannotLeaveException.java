package com.studiz.domain.study.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class StudyOwnerCannotLeaveException extends CustomException {
    
    public StudyOwnerCannotLeaveException() {
        super(ErrorCode.STUDY_OWNER_CANNOT_LEAVE);
    }
}

