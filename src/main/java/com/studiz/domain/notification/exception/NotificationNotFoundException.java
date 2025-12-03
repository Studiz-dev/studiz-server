package com.studiz.domain.notification.exception;

import com.studiz.global.exception.CustomException;
import com.studiz.global.exception.ErrorCode;

public class NotificationNotFoundException extends CustomException {

    public NotificationNotFoundException() {
        super(ErrorCode.NOTIFICATION_NOT_FOUND);
    }
}






