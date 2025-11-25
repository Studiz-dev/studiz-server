package com.studiz.domain.study.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class StudyNotFoundException extends CustomException {
    
    public StudyNotFoundException() {
        super(ErrorCode.STUDY_NOT_FOUND);
    }
}

