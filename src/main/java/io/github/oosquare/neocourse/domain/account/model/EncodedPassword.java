package io.github.oosquare.neocourse.domain.account.model;

import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.utility.exception.ValueValidationException;

@Value
public class EncodedPassword {

    private final @NonNull String value;

    private EncodedPassword(@NonNull String value) {
        ValueValidationException.validator()
            .ensure(!value.isBlank())
            .message("Encoded password should not be blank")
            .done();
        this.value = value;
    }

    public static EncodedPassword of(@NonNull String value) {
        return new EncodedPassword(value);
    }

    public static EncodedPassword ofInternally(@NonNull String value) {
        return EncodedPassword.of(value);
    }
}
