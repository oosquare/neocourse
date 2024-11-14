package io.github.oosquare.neocourse.domain.account.exception;

import io.github.oosquare.neocourse.domain.common.exception.DomainException;

public class AccountException extends DomainException {

    public AccountException(String message) {
        super(message);
    }

    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
