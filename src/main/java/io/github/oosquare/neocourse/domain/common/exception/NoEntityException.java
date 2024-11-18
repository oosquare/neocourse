package io.github.oosquare.neocourse.domain.common.exception;

public class NoEntityException extends DomainException {

    public NoEntityException(String message) {
        super(message);
    }

    public NoEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
