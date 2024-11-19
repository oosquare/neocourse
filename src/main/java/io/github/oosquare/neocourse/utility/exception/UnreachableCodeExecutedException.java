package io.github.oosquare.neocourse.utility.exception;

import java.util.Map;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;

public class UnreachableCodeExecutedException extends NeoCourseException {

    private static final String DEFAULT_USER_MESSAGE = "An unreachable error occurred";

    @Builder
    public UnreachableCodeExecutedException(
        @NonNull String message,
        @Singular("context") Map<String, Object> context,
        Throwable cause
    ) {
        super(message, DEFAULT_USER_MESSAGE, context, cause);
    }
}
