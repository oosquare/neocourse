package io.github.oosquare.neocourse.utility.exception;

import java.util.Map;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;

public class EntityNotFoundException extends NeoCourseException {

    private static final String DEFAULT_USER_MESSAGE = "Could not find anything with given inputs";

    @Builder
    public EntityNotFoundException(
        @NonNull Class<?> entity,
        @Singular("context") Map<String, Object> context,
        Throwable cause
    ) {
        super(
            String.format("Could not find entity %s", entity.getName()),
            DEFAULT_USER_MESSAGE,
            context,
            cause
        );
    }
}
