package io.github.oosquare.neocourse.domain.plan.exception;

import io.github.oosquare.neocourse.domain.common.exception.DomainException;

public class CourseSetException extends DomainException {

    public CourseSetException(String message) {
        super(message);
    }

    public CourseSetException(String message, Throwable cause) {
        super(message, cause);
    }
}
