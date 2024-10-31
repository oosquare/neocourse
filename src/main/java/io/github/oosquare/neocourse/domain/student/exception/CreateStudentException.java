package io.github.oosquare.neocourse.domain.student.exception;

import io.github.oosquare.neocourse.domain.common.exception.DomainException;

public class CreateStudentException extends DomainException {

    public CreateStudentException(String message) {
        super(message);
    }

    public CreateStudentException(String message, Throwable cause) {
        super(message, cause);
    }
}
