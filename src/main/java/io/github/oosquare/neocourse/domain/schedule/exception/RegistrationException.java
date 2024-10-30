package io.github.oosquare.neocourse.domain.schedule.exception;

import io.github.oosquare.neocourse.domain.common.exception.DomainException;

public class RegistrationException extends DomainException {

    public RegistrationException(String message) {
        super(message);
    }

    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
