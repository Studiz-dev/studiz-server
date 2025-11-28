package com.studiz.domain.schedule.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class ScheduleAccessDeniedException extends CustomException {

    public ScheduleAccessDeniedException() {
        super(ErrorCode.SCHEDULE_ACCESS_DENIED);
    }
}

