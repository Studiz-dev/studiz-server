package com.studiz.domain.study.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class StudyOwnerCannotBeRemovedException extends CustomException {
    
    public StudyOwnerCannotBeRemovedException() {
        super(ErrorCode.STUDY_OWNER_CANNOT_BE_REMOVED);
    }
}

