package io.github.oosquare.neocourse.domain.admin.exception;

import io.github.oosquare.neocourse.domain.common.exception.DomainException;

public class CreateAdministratorException extends DomainException {

    public CreateAdministratorException(String message) {
        super(message);
    }

    public CreateAdministratorException(String message, Throwable cause) {
        super(message, cause);
    }
}
