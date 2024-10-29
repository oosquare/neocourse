package io.github.oosquare.neocourse.domain.plan.exception;

import io.github.oosquare.neocourse.domain.common.exception.DomainException;

public class PlanException extends DomainException {

    public PlanException(String message) {
        super(message);
    }

    public PlanException(String message, Throwable cause) {
        super(message, cause);
    }
}
