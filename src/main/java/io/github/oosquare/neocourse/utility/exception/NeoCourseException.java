package io.github.oosquare.neocourse.utility.exception;

import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class NeoCourseException extends RuntimeException {

    public static final String DEFAULT_USER_MESSAGE = "An error occurred";

    private final @NonNull String userMessage;
    private final @NonNull Map<String, Object> context;

    public NeoCourseException(
        @NonNull String message,
        String userMessage,
        Map<String, Object> context,
        Throwable cause
    ) {
        super(message, cause);
        this.userMessage = Optional.ofNullable(userMessage).orElse(DEFAULT_USER_MESSAGE);
        this.context = Optional.ofNullable(context).orElse(Map.of());
    }

    @Override
    public String getMessage() {
        if (this.getContext().isEmpty()) {
            return super.getMessage();
        } else {
            String contextString = context.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
            return super.getMessage() + " [" + contextString + "]";
        }
    }
}
