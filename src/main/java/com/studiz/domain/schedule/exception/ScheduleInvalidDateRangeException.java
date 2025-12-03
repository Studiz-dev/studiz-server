package com.studiz.domain.schedule.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class ScheduleInvalidDateRangeException extends CustomException {

    public ScheduleInvalidDateRangeException() {
        super(ErrorCode.SCHEDULE_INVALID_DATE_RANGE);
    }
}






