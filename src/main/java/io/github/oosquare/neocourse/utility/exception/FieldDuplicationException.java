package io.github.oosquare.neocourse.utility.exception;

import java.util.Map;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;

public class FieldDuplicationException extends DomainException {

    @Builder
    public FieldDuplicationException(
        @NonNull String message,
        @NonNull String userMessage,
        @Singular("context") Map<String, Object> context,
        Throwable cause
    ) {
        super(message, userMessage, context, cause);
    }
}
