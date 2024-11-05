package io.github.oosquare.neocourse.domain.course.exception;

import io.github.oosquare.neocourse.domain.common.exception.DomainException;

public class RemoveCourseException extends DomainException {

    public RemoveCourseException(String message) {
        super(message);
    }

    public RemoveCourseException(String message, Throwable cause) {
        super(message, cause);
    }
}
