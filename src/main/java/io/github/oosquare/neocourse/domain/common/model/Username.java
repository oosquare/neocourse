package io.github.oosquare.neocourse.domain.common.model;

import java.util.regex.Pattern;

import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import io.github.oosquare.neocourse.utility.exception.ValueValidationException;

@Value
@ToString(includeFieldNames = false)
public class Username {

    private static Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9\\-_]+");
    private String value;

    private Username(String value) {
        ValueValidationException.validator()
            .ensure(!value.isBlank())
            .message("Username should not be blank")
            .done();
        ValueValidationException.validator()
            .ensure(USERNAME_PATTERN.matcher(value).matches())
            .message("Username should only contains ASCII alphabets, numbers, hyphens and underscores")
            .done();
        this.value = value;
    }

    public static Username of(@NonNull String value) {
        return new Username(value);
    }

    public static Username ofInternally(@NonNull String value) {
        return Username.of(value);
    }
}
