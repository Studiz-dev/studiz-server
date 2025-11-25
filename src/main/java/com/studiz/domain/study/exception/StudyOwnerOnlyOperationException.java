package com.studiz.domain.study.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class StudyOwnerOnlyOperationException extends CustomException {
    
    public StudyOwnerOnlyOperationException() {
        super(ErrorCode.STUDY_OWNER_REQUIRED);
    }
}

