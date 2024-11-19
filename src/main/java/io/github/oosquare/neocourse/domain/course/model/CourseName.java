package io.github.oosquare.neocourse.domain.course.model;

import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import io.github.oosquare.neocourse.utility.exception.ValueValidationException;

@Value
@ToString(includeFieldNames = false)
public class CourseName {

    private final @NonNull String value;

    private CourseName(@NonNull String value) {
        ValueValidationException.validator()
            .ensure(!value.isBlank())
            .message("Course name should not be blank")
            .done();
        ValueValidationException.validator()
            .ensure(value.strip().equals(value))
            .message("Course name should not have leading and trailing spaces")
            .done();
        this.value = value;
    }

    public static CourseName of(@NonNull String value) {
        return new CourseName(value);
    }

    public static CourseName ofInternally(@NonNull String value) {
        return CourseName.of(value);
    }
}
