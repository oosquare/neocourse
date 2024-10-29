package io.github.oosquare.neocourse.domain.teacher.exception;

import io.github.oosquare.neocourse.domain.common.exception.DomainException;

public class TeacherException extends DomainException {

    public TeacherException(String message) {
        super(message);
    }

    public TeacherException(String message, Throwable cause) {
        super(message, cause);
    }
}
