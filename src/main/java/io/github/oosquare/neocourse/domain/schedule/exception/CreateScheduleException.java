package io.github.oosquare.neocourse.domain.schedule.exception;

import io.github.oosquare.neocourse.domain.common.exception.DomainException;

public class CreateScheduleException extends DomainException {

    public CreateScheduleException(String message) {
        super(message);
    }

    public CreateScheduleException(String message, Throwable cause) {
        super(message, cause);
    }
}
