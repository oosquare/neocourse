package io.github.oosquare.neocourse.domain.schedule.exception;

import io.github.oosquare.neocourse.domain.common.exception.DomainException;

public class EvaluationException extends DomainException {

    public EvaluationException(String message) {
        super(message);
    }

    public EvaluationException(String message, Throwable cause) {
        super(message, cause);
    }
}
