package io.github.oosquare.neocourse.domain.teacher.exception;

import io.github.oosquare.neocourse.domain.common.exception.DomainException;

public class OfferedCourseException extends DomainException {

    public OfferedCourseException(String message) {
        super(message);
    }

    public OfferedCourseException(String message, Throwable cause) {
        super(message, cause);
    }
}
