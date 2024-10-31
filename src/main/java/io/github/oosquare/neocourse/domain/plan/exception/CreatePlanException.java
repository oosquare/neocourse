package io.github.oosquare.neocourse.domain.plan.exception;

import io.github.oosquare.neocourse.domain.common.exception.DomainException;

public class CreatePlanException extends DomainException {

    public CreatePlanException(String message) {
        super(message);
    }

    public CreatePlanException(String message, Throwable cause) {
        super(message, cause);
    }
}
