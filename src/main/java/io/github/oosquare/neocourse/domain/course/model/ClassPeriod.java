package io.github.oosquare.neocourse.domain.course.model;

import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import io.github.oosquare.neocourse.utility.exception.ValueValidationException;

@Value
@ToString(includeFieldNames = false)
public class ClassPeriod {

    private static final ClassPeriod VALUE_0 = new ClassPeriod(0);
    private static final ClassPeriod VALUE_1 = new ClassPeriod(1);
    private static final ClassPeriod VALUE_2 = new ClassPeriod(2);
    private static final ClassPeriod VALUE_3 = new ClassPeriod(3);
    private static final ClassPeriod VALUE_4 = new ClassPeriod(4);

    private final int value;

    private ClassPeriod(int value) {
        ValueValidationException.validator()
            .ensure(value >= 0)
            .message("Class period should not be negative")
            .done();
        this.value = value;
    }

    public static ClassPeriod of(int value) {
        return switch (value) {
            case 0 -> ClassPeriod.VALUE_0;
            case 1 -> ClassPeriod.VALUE_1;
            case 2 -> ClassPeriod.VALUE_2;
            case 3 -> ClassPeriod.VALUE_3;
            case 4 -> ClassPeriod.VALUE_4;
            default -> new ClassPeriod(value);
        };
    }

    public static ClassPeriod ofInternally(int value) {
        return ClassPeriod.of(value);
    }

    public ClassPeriod plus(@NonNull ClassPeriod other) {
        return ClassPeriod.of(this.getValue() + other.getValue());
    }

    public ClassPeriod minus(@NonNull ClassPeriod other) {
        return ClassPeriod.of(this.getValue() - other.getValue());
    }

    public ClassPeriod withUpperBound(@NonNull ClassPeriod bound) {
        if (this.isExceedUpperBound(bound)) {
            return bound;
        } else {
            return this;
        }
    }

    public boolean isExceedUpperBound(@NonNull ClassPeriod bound) {
        return this.getValue() > bound.getValue();
    }
}
