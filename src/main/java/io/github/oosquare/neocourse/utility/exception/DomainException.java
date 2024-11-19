package io.github.oosquare.neocourse.utility.exception;

import java.util.Map;

import lombok.NonNull;

public class DomainException extends NeoCourseException {

    public DomainException(
        @NonNull String message,
        @NonNull String userMessage,
        Map<String, Object> context,
        Throwable cause
    ) {
        super(message, userMessage, context, cause);
    }
}
