package io.github.oosquare.neocourse.domain.plan.model;

import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import io.github.oosquare.neocourse.utility.exception.ValueValidationException;

@Value
@ToString(includeFieldNames = false)
public class PlanName {

    private final @NonNull String value;

    private PlanName(String value) {
        ValueValidationException.validator()
            .ensure(!value.isBlank())
            .message("Plan name should not be blank")
            .done();
        ValueValidationException.validator()
            .ensure(value.strip().equals(value))
            .message("Plan name should not have leading and trailing spaces")
            .done();
        this.value = value;
    }

    public static PlanName of(@NonNull String value) {
        return new PlanName(value);
    }

    public static PlanName ofInternally(@NonNull String value) {
        return PlanName.of(value);
    }
}
