package io.github.oosquare.neocourse.utility.exception;

import java.util.Map;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;

public class ValueValidationException extends NeoCourseException {

    @Builder
    public ValueValidationException(
        @NonNull String message,
        @Singular("context") Map<String, Object> context
    ) {
        super(message, message, context, null);
    }

    public static ValueValidator validator() {
        return new ValueValidator();
    }

    public static class ValueValidator {

        private ValueValidationException.ValueValidationExceptionBuilder builder;

        ValueValidator() {
            this.builder = null;
        }

        public ValueValidator ensure(boolean expression) {
            if (!expression) {
                this.builder = builder();
            }
            return this;
        }

        public ValueValidator message(@NonNull String message) {
            if (this.builder != null) {
                this.builder.message(message);
            }
            return this;
        }

        public ValueValidator context(Map<? extends String, ?> context) {
            if (this.builder != null) {
                this.builder.context(context);
            }
            return this;
        }

        public ValueValidator context(String contextKey, Object contextValue) {
            if (this.builder != null) {
                this.builder.context(contextKey, contextValue);
            }
            return this;
        }

        public void done() {
            if (this.builder != null) {
                throw this.builder.build();
            }
        }
    }
}
