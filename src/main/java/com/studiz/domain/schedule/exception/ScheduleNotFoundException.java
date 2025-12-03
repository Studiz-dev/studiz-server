package com.studiz.domain.schedule.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class ScheduleNotFoundException extends CustomException {

    public ScheduleNotFoundException() {
        super(ErrorCode.SCHEDULE_NOT_FOUND);
    }
}






