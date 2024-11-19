package io.github.oosquare.neocourse.domain.schedule.model;

import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import io.github.oosquare.neocourse.utility.exception.ValueValidationException;

@Value
@ToString(includeFieldNames = false)
public class Place {

    private final @NonNull String value;

    private Place(@NonNull String value) {
        ValueValidationException.validator()
            .ensure(!value.isBlank())
            .message("Place should not be blank")
            .done();
        this.value = value;
    }

    public static Place of(@NonNull String value) {
        return new Place(value);
    }

    public static Place ofInternally(@NonNull String value) {
        return Place.of(value);
    }
}
