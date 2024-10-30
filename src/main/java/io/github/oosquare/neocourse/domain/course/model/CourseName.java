package io.github.oosquare.neocourse.domain.course.model;

import lombok.NonNull;
import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public class CourseName {

    private final @NonNull String value;

    private CourseName(@NonNull String value) {
        checkArgument(!value.isBlank(), "Course name should not be blank");
        checkArgument(
            value.strip().equals(value),
            "Course name should not have leading and trailing spaces"
        );
        this.value = value;
    }

    public static CourseName of(@NonNull String value) {
        return new CourseName(value);
    }

    public static CourseName ofInternally(@NonNull String value) {
        return CourseName.of(value);
    }
}
