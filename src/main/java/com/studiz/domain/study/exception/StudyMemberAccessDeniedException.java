package com.studiz.domain.study.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class StudyMemberAccessDeniedException extends CustomException {
    
    public StudyMemberAccessDeniedException() {
        super(ErrorCode.STUDY_MEMBER_ACCESS_DENIED);
    }
}

