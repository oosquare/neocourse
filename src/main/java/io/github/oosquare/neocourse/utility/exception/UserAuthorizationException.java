package io.github.oosquare.neocourse.utility.exception;

import java.util.Map;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;

public class UserAuthorizationException extends NeoCourseException {

    @Builder
    public UserAuthorizationException(
        @NonNull String message,
        @NonNull String userMessage,
        @Singular("context") Map<String, Object> context
    ) {
        super(message, userMessage, context, null);
    }
}
