package com.studiz.domain.study.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class StudyMemberAlreadyOwnerException extends CustomException {
    
    public StudyMemberAlreadyOwnerException() {
        super(ErrorCode.STUDY_MEMBER_ALREADY_OWNER);
    }
}

