package io.github.oosquare.neocourse.domain.course.exception;

import io.github.oosquare.neocourse.domain.common.exception.DomainException;

public class CreateCourseException extends DomainException {

    public CreateCourseException(String message) {
        super(message);
    }

    public CreateCourseException(String message, Throwable cause) {
        super(message, cause);
    }
}
