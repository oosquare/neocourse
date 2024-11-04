package io.github.oosquare.neocourse.domain.schedule.exception;

import io.github.oosquare.neocourse.domain.common.exception.DomainException;

public class ScheduleException extends DomainException {

    public ScheduleException(String message) {
        super(message);
    }

    public ScheduleException(String message, Throwable cause) {
        super(message, cause);
    }
}
