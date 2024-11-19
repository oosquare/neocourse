package io.github.oosquare.neocourse.domain.common.model;

import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import io.github.oosquare.neocourse.utility.exception.ValueValidationException;

@Value
@ToString(includeFieldNames = false)
public class DisplayedUsername {

    private final @NonNull String value;

    private DisplayedUsername(String value) {
        ValueValidationException.validator()
            .ensure(!value.isBlank())
            .message("Displayed username should not be blank")
            .done();
        ValueValidationException.validator()
            .ensure(value.strip().equals(value))
            .message("Displayed username should not have leading and trailing spaces")
            .done();
        this.value = value;
    }

    public static DisplayedUsername of(@NonNull String value) {
        return new DisplayedUsername(value);
    }

    public static DisplayedUsername ofInternally(@NonNull String value) {
        return DisplayedUsername.of(value);
    }
}
