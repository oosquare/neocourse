package io.github.oosquare.neocourse.domain.common.exception;

public class UserException extends DomainException {

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
