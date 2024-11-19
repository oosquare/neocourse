package io.github.oosquare.neocourse.domain.schedule.model;

import lombok.ToString;
import lombok.Value;

import io.github.oosquare.neocourse.utility.exception.ValueValidationException;

@Value
@ToString(includeFieldNames = false)
public class Capacity {

    private final int value;

    private Capacity(int value) {
        ValueValidationException.validator()
            .ensure(value > 0)
            .message("Capacity should be positive")
            .done();
        this.value = value;
    }

    public static Capacity of(int value) {
        return new Capacity(value);
    }

    public static Capacity ofInternally(int value) {
        return Capacity.of(value);
    }
}
