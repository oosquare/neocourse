package io.github.oosquare.neocourse.domain.schedule.exception;

import io.github.oosquare.neocourse.domain.common.exception.DomainException;

public class EvaluationSpecificationException extends DomainException {

    public EvaluationSpecificationException(String message) {
        super(message);
    }

    public EvaluationSpecificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
