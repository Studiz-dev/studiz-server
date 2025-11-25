package com.studiz.domain.study.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class StudyMemberNotFoundException extends CustomException {
    
    public StudyMemberNotFoundException() {
        super(ErrorCode.STUDY_MEMBER_NOT_FOUND);
    }
}

