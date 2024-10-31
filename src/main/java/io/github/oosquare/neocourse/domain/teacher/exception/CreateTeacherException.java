package io.github.oosquare.neocourse.domain.teacher.exception;

import io.github.oosquare.neocourse.domain.common.exception.DomainException;

public class CreateTeacherException extends DomainException {

    public CreateTeacherException(String message) {
        super(message);
    }

    public CreateTeacherException(String message, Throwable cause) {
        super(message, cause);
    }
}
