package io.github.oosquare.neocourse.domain.transcript.exception;

import io.github.oosquare.neocourse.domain.common.exception.DomainException;

public class TranscriptException extends DomainException {

    public TranscriptException(String message) {
        super(message);
    }

    public TranscriptException(String message, Throwable cause) {
        super(message, cause);
    }
}
